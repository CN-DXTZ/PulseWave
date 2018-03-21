import numpy as np

sampling_rate = 2 ** 14
fft_size = 2 ** 12
t = np.arange(0, 1, 1.0 / sampling_rate)
x = np.array(map(lambda x: x * 1e3, t))
y = np.sqrt(2) * np.sin(2 * np.pi * 1000 * t)
y = y + 0.005 * np.random.normal(0.0, 1.0, len(y))
# fft
ys = y[:fft_size]
yf = np.fft.rfft(ys) / fft_size
freq = np.linspace(0, sampling_rate / 2, fft_size / 2 + 1)
freqs = np.array(map(lambda x: x / 1e3, freq))
yfp = 20 * np.log10(np.clip(np.abs(yf), 1e-20, 1e100))
