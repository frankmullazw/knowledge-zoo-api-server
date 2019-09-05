"""
Aim: Read all the APK file and use extractInfo.py to extract the information.
Reference: https://www.jianshu.com/p/670023af50f6
"""
#-*- coding:GBK -*-
import os
import os.path
import sys
import subprocess
import extractInfo
import setNeo4jNode

rootdir = "C:/Users/caila/Desktop/FIT4003/APK Files/APK"
destdir = "C:/Users/caila/Desktop/FIT4003/androguard-master/Information/JSON/"

class Packages:
    def __init__(self, srcdir, desdir):
        self.sdir = srcdir
        self.ddir = desdir

    def checkFile(self):
        # Used for "toOutputFile" in extractInfo.
        # It will extract infomation in APK and save it to txt / JSON format.
        print("--------------------starting unpackage!---------------------")
        for dirpath, dirnames, filenames in os.walk(rootdir):
            for filename in filenames:
                thefile = os.path.join(dirpath, filename)
                apkfile = os.path.split(thefile)[1]
                apkname = os.path.splitext(apkfile)[0]
                print (apkfile)
                try:
                    if os.path.splitext(thefile)[1] == ".apk":
                        # name = os.path.splitext(thefile)[0]
                        #str1= '"'+thefile+'"'
                        #str2= '"'+destdir + os.path.splitext(filename)[0]+'"'
                        # cmdExtract = r'%s d -f %s %s'% (command, str2, str1)
                        extractInfo.toOutputFile(thefile, self.ddir)
                        print ("******************well done unpacked******************")
                except IOError as err:
                        print (err)
                        sys.exit()

    def checkNode(self, db):
        # Used for "main" in setNeo4jNode.
        # It will get in APK, create node / update the relationship in KG
        print("--------------------start creating nodes!---------------------")
        for dirpath, dirnames, filenames in os.walk(rootdir):
            for filename in filenames:
                thefile = os.path.join(dirpath, filename)
                apkfile = os.path.split(thefile)[1]
                apkname = os.path.splitext(apkfile)[0]
                print (apkfile)
                try:
                    if os.path.splitext(thefile)[1] == ".apk":
                        setNeo4jNode.main(thefile, db)
                        print ("******************well done created******************")
                except IOError as err:
                        print (err)
                        sys.exit()

if __name__ == "__main__":
    dir = Packages(rootdir, destdir)
    # To output JSON File
    dir.checkFile()

    # To create node
    # address = "http://localhost:7474/db/data"
    # username = "neo4j"
    # password = "1234"
    # db = setNeo4jNode.connectDB(address, username, password)
    # dir.checkNode(db)