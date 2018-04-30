import threading
import SerialData
import DynamicWave
import time

if __name__ == '__main__':
    threading.Thread(target=SerialData.ReadDataToSQL, args=(11,)).start()
    time.sleep(2)
    DynamicWave.Display()
