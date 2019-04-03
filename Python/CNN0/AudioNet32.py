# -*- coding: utf-8 -*-
"""
Created on Fri Jan 19 12:48:21 2018

@author: Vijay Anand
"""

import keras
import scipy.io.wavfile
from scipy.signal import spectrogram
import os
import numpy as np
from sklearn.cross_validation import train_test_split
import pickle
from keras import optimizers
from keras.models import Sequential
from sklearn import decomposition
from sklearn.metrics import accuracy_score
import random
from keras.layers.convolutional import Conv1D
from keras.layers.convolutional import MaxPooling1D
import matplotlib.pyplot as plt
import time
from sklearn import preprocessing
from keras.layers.core import Dense, Dropout, Activation, Flatten
from keras.layers.embeddings import Embedding
from keras.layers import LSTM
import pprint


# ==============================================================================
# Shuffle 2 lists together
# ==============================================================================

def shuffle_2lists(l1, l2):
    z = list(zip(l1, l2))
    random.shuffle(z)

    l1, l2 = zip(*z)

    return l1, list(l2)


# ==============================================================================
# Generate minibatch on the fly
# ==============================================================================

def fetch_minibatch(l1, l2, size):
    l1, l2 = shuffle_2lists(l1, l2)

    o1 = l1[:size]
    o2 = l2[:size]

    o1 = np.array([read_aud(path) for path in o1])
    o1 = np.reshape(o1, (o1.shape[0], o1.shape[1], 1))

    return o1, o2


# ==============================================================================
# Read audiofile and pad with zeros
# ==============================================================================

def read_aud(path):
    rate, audio = scipy.io.wavfile.read(path)

    audio = audio[:15500]
    pad_array = np.zeros(16000 - (audio.shape[0]))
    pad_aud = np.hstack((audio, pad_array))

    noise = np.random.randint(low=-200, high=200, size=16000)
    pad_aud = pad_aud + noise

    arr = np.roll(pad_aud, random.randint(-250, 250))

    int_arr = preprocessing.scale(arr)

    return int_arr


# ==============================================================================
# Generate trainval data
# ==============================================================================

def get_trainval_list(froot, train_ratio):
    flist = os.listdir(froot)

    synset = {cat: indx for indx, cat in enumerate(flist)}

    audio = []
    gt = []

    for label, key in synset.items():
        files = os.listdir(os.path.join(froot, label))
        aud = [os.path.join(froot, label, f) for f in files]
        audio.extend(aud)
        labels = np.empty(len(files), dtype=np.int)
        labels.fill(key)
        labels = labels.tolist()
        gt.extend(labels)

    audio, gt = shuffle_2lists(audio, gt)

    train = audio[:int(len(audio) * train_ratio)]
    train_gt = gt[:int(len(audio) * train_ratio)]

    val = audio[int(len(audio) * train_ratio):]
    val_gt = gt[int(len(audio) * train_ratio):]

    return (train, train_gt, val, val_gt, synset)


# ==============================================================================
# Model architecture
# ==============================================================================

def model_init():
    model = Sequential()

    model.add(
        Conv1D(filters=256, kernel_size=32, strides=4, padding='valid', activation='relu', input_shape=(16000, 1)))
    model.add(Conv1D(filters=256, kernel_size=32, strides=4, padding='valid', activation='relu',
                     kernel_initializer='glorot_normal'))
    model.add(Conv1D(filters=256, kernel_size=32, strides=4, padding='valid', activation='relu',
                     kernel_initializer='glorot_normal'))
    model.add(Conv1D(filters=256, kernel_size=32, strides=4, padding='valid', activation='relu',
                     kernel_initializer='glorot_normal'))
    model.add(Conv1D(filters=256, kernel_size=32, strides=4, padding='valid', activation='relu',
                     kernel_initializer='glorot_normal'))
    #    model.add(Conv1D(filters=128, kernel_size=32, strides=4, padding='valid', activation='relu', kernel_initializer='glorot_normal'))

    model.add(Flatten())

    model.add(Dense(512, activation='relu', bias_initializer='zero'))
    model.add(Dropout(0.2))
    model.add(Dense(512, activation='relu', bias_initializer='zero'))
    model.add(Dropout(0.2))
    model.add(Dense(512, activation='relu', bias_initializer='zero'))
    model.add(Dropout(0.2))
    model.add(Dense(512, activation='relu', bias_initializer='zero'))

    model.add(Dense(31, activation='softmax'))

    o = optimizers.Nadam(lr=0.0001, beta_1=0.9, beta_2=0.999, epsilon=1e-08)
    model.compile(loss='sparse_categorical_crossentropy', optimizer=o, metrics=['accuracy'])

    model.summary()

    model_json = model.to_yaml()

    with open("model_32.yaml", "w") as json_file:
        json_file.write(model_json)

    return model


# ==============================================================================
# Validation
# ==============================================================================

def validate_model(val, val_gt, model, val_samples):
    mtest, mt_gt = fetch_minibatch(val, val_gt, val_samples)
    predictions = model.predict(mtest)
    print('')
    print('++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++')
    print('')
    print('Validation Accuracy: ', accuracy_score(np.argmax(predictions, axis=1), mt_gt))
    print('++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++')


# ==============================================================================
# Resume training
# ==============================================================================

def resume_training(model, weights):
    with open('trained_models/train_data_dic.pkl', 'rb') as handle:
        x = pickle.load(handle)

    train = x['train']
    train_gt = x['train_gt']
    val = x['val']
    val_gt = x['val_gt']
    synset = ['synset']

    model.load_weights(weights)

    return (val, val_gt, train, train_gt, synset, model)


# ==============================================================================
# Main
# ==============================================================================
def main():
    pp = pprint.PrettyPrinter(indent=4)

    data_path = '/media/vijay/Vijay/Datasets/Speech/data_speech_commands_v0.01/'
    train_ratio = 0.9
    train, train_gt, val, val_gt, synset = get_trainval_list(data_path, train_ratio)

    train_data_dic = {'train': train, 'train_gt': train_gt, 'val': val, 'val_gt': val_gt,
                      'synset': synset}

    print('++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++')
    print('Train files: ', len(train), ' & val files: ', len(val))
    pp.pprint(synset)
    print('++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++')

    with open('train_data_dic.pkl', 'wb') as handle:
        pickle.dump(train_data_dic, handle, protocol=pickle.HIGHEST_PROTOCOL)

    model = model_init()
    csv_logger = keras.callbacks.CSVLogger('training.log', append=True)

    ## Resume

    #    weights='epoch_10.h5'

    #    train,train_gt,val,val_gt,synset,model=resume_training(model,weights)

    # Training

    batch_size = 128
    minibatches = int(len(train_gt) / batch_size)
    start = time.time()

    for e in range(10):

        for minibatch in range(minibatches):
            mtrain, m_gt = fetch_minibatch(train, train_gt, batch_size)

            print('')
            print('Minibatch no ', minibatch, ' of ', minibatches, ' in epoch no ', e)
            print('')
            model.fit(mtrain, m_gt, epochs=1, batch_size=batch_size, callbacks=[csv_logger])
            print('')
            print('Approx Time elapsed: ', int((time.time() - start) / 60), ' minutes')
            print('')
            print('*****************************************************************************************')

        validate_model(val, val_gt, model, 2048)

        if e % 2 == 0:
            model.save('trained_models/epoch_' + str(e) + '.h5')

    model.save('trained_models/epoch_' + str(e) + '_final.h5')


if __name__ == '__main__':
    main()
