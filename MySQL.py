import MySQLdb as mysql

def MysqlInit():
    db = mysql.connect(user="root", passwd="1234", db="wave", host="localhost")
    db.autocommit(True)
    cur = db.cursor()
    return cur
