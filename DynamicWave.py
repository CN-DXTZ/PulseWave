import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation


def display(WAVE):
    fig, ax = plt.subplots()
    x = np.arange(400)
    y = np.zeros(400)
    plt.xticks([])
    line, = ax.plot(x, y)
    ax.set_xlim(0, 400)
    ax.set_ylim(1586000, 1602000)

    def update(frames):
        line.set_ydata(WAVE[x + frames])
        return line,

    ani = animation.FuncAnimation(fig, update, interval=2, blit=True)
    plt.show()
