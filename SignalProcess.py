import numpy as np
import matplotlib.pyplot as plt
import PulseWave
import matplotlib as mpl


# 平滑并绘图
def smooth(x, n=20):
    N = len(x)

    weight = np.ones(n)
    weight /= weight.sum()
    x_sma = np.convolve(x, weight, mode='valid')  # 简单移动平均

    weight = np.linspace(1, 0, n)
    weight = np.exp(weight)
    weight /= weight.sum()
    x_ema = np.convolve(x, weight, mode='valid')  # 指数移动平均

    mpl.rcParams['font.sans-serif'] = [u'SimHei']
    mpl.rcParams['axes.unicode_minus'] = False
    plt.figure(facecolor='w')
    plt.plot(np.arange(N), x, 'r-', linewidth=1, label=u'原始')
    t = np.arange(n - 1, N)
    plt.plot(t, x_sma, 'b-', linewidth=1, label=u'简单移动平均线')
    plt.plot(t, x_ema, 'g-', linewidth=1, label=u'指数移动平均线')
    plt.legend(loc='upper right')
    plt.grid(True)
    plt.show()


def ft(x0):
    mpl.rcParams['font.sans-serif'] = [u'SimHei']
    mpl.rcParams['axes.unicode_minus'] = False

    n = 20
    N = len(x0)
    x = ((x0 - np.mean(x0)) / (np.max(x0) - np.min(x0))).flat

    # 指数移动平均
    weight = np.linspace(1, 0, n)
    weight = np.exp(weight)
    weight /= weight.sum()
    x_ema = np.convolve(x, weight, mode='valid')
    t = np.arange(n - 1, N)

    # 傅里叶变换(原始数据)
    # plt.subplot(212)
    N2 = len(x)
    w = np.arange(N2) * 2 * np.pi / N2
    f = np.fft.fft(x)
    a = np.abs(f / N2)
    # plt.stem(w, a)

    # 傅里叶变换(平滑后数据)
    # plt.subplot(212)
    # N2 = len(x_ema)
    # w = np.arange(N2) * 2 * np.pi / N2
    # f = np.fft.fft(x_ema)
    # a = np.abs(f / N2)
    # plt.stem(w, a)

    # 还原
    f_real = np.real(f)
    lim = 0.4
    eps = lim * f_real.max()
    f_real[(f_real < eps) & (f_real > -eps)] = 0
    f_imag = np.imag(f)
    eps = lim * f_imag.max()
    f_imag[(f_imag < eps) & (f_imag > -eps)] = 0
    f1 = f_real + f_imag * 1j
    y1 = np.fft.ifft(f1)
    y1 = np.real(y1)

    # plt.subplot(211)
    plt.plot(t, x_ema, label=u'指数移动平均线')
    plt.plot(x, label=u'原始数据')
    plt.plot(y1, label=u'逆变换')

    plt.legend(loc='upper left')
    plt.show()


if __name__ == '__main__':
    x0 = PulseWave.DataLoad('副本5.txt')

    ft(x0[:500, 0])
