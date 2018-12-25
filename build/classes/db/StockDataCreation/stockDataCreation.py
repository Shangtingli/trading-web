import pandas as pd
import numpy as np
import requests,re
from bs4 import BeautifulSoup
import MySQLdb


def getWebPage(url,params,alphabet):
    return url + params + alphabet

def getOnePageInfo(webpage):
    page = requests.get(webpage)
    soup = BeautifulSoup(page.text,'html.parser')
    names= []
    symbols =[]
    select_soup1 = soup.select('.ts0')
    select_soup2 = soup.select('.ts1')
    for soup in select_soup1:
        name = soup.select('td')[0].text.lstrip().rstrip()
        symbol = soup.select('td')[1].text.lstrip().rstrip()
        names.append(name)
        symbols.append(symbol)
    
    for soup in select_soup2:
        name = soup.select('td')[0].text
        symbol = soup.select('td')[1].text
        names.append(name)
        symbols.append(symbol)

    return names,symbols

def getAllNASDAQInfo():
    NAMES = []
    SYMBOLS = []
    TAGS = [chr(ord('A') + i) for i in range(26)] 
    url = 'http://www.advfn.com/nasdaq/nasdaq.asp'
    params = '?companies='
    for i,tag in enumerate(TAGS):
        webpage = getWebPage(url,params,tag)
        names, symbols = getOnePageInfo(webpage)
        NAMES += names
        SYMBOLS += symbols

    return NAMES, SYMBOLS

NAMES,SYMBOLS = getAllNASDAQInfo()
HOST = "localhost"
USERNAME = "root"
PASSWORD = "root"
DATABASE = 'trading'
conn = MySQLdb.connect(HOST,USERNAME,PASSWORD,DATABASE)
cursor = conn.cursor()
stm = "DROP TABLE IF EXISTS tickers"
cursor.execute(stm)
stm = "CREATE TABLE tickers("\
    + "symbol VARCHAR(255) NOT NULL,"\
    + "name VARCHAR(255) NOT NULL,"\
    + "PRIMARY KEY (symbol)"\
    + ")"
cursor.execute(stm)
for i in range(len(NAMES)):
    name = NAMES[i]
    symbol = SYMBOLS[i]
    stm = "INSERT INTO tickers VALUES (\"" + str(symbol) + "\",\""\
    + str(name) +"\");"
    print(stm)
    cursor.execute(stm)

try:
    conn.commit()
except:
    conn.rollback()
conn.close()
