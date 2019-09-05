from neo4jrestclient.client import GraphDatabase
from neo4jrestclient import client
from neo4jrestclient.query import Q
import extractInfo


def addAPKNode(db, name, sha256):
    # Check if APK node exist
    print("Checking if APK Node exist.")
    apk = db.labels.get("APK")
    filteredNode = apk.filter(Q("name", exact = name))
    if len(filteredNode) == 0:
        createdNode = db.nodes.create(name=name,sha256=sha256)
        db.labels.get("APK").add(createdNode)
        print("Added new node: ",name)
    else: 
        for i in filteredNode:
            createdNode = i
        print("APK Node existed: ", name)
    return createdNode


def addPermissionNode(db, name):
    # Check if Permission Node exist.
    print("Checking if Permission Node exist.")
    per = db.labels.get("Permission")
    filteredNode = per.filter(Q("name", exact = name))
    if len(filteredNode) == 0:
        createdNode = db.nodes.create(name=name)
        db.labels.get("Permission").add(createdNode)
        print("Added new node: ",name)
    else: 
        for i in filteredNode:
            createdNode = i
        print("Permission Node existed: ",name)
    return createdNode


def createApkApiRelation(db, APIName, APKNode, countError):
    #Check if the api in KG exist for the apk, if yes, create a relationship and link it
    APIName = APIName.strip() # strip empty space
    APINode = db.labels.get("API").get(name=APIName)
    if len(APINode) != 0:
        # APK use API
        node = APINode.next()
        APKNode.relationships.create("use",node)
    else:
        countError += 1
        print("Error:"+APIName)
    return countError


def createApkPerRelation(db, APKNode, PerNode):
    # Not checking the exstence of relationship as it will take too much resources.
    APKNode.relationships.create("get", PerNode)
    print("Relationship created between",APKNode["name"],PerNode["name"])


def readAPIText(filename):
    file = open(filename, "r")
    count = 0
    APIList= []
    for line in file:
        if count > 2: # skip 3 lines
            APIList.append(line)
        count += 1
    return APIList


def connectDB(address, username, password):
    db = GraphDatabase(address, username, password)
    return db


def main(path, db):
    print("Creating All Nodes...")

    #Get the infomation of APK
    name, sha256, permission = extractInfo.toOutputNode(path)

    # Add APK Node
    apkNode = addAPKNode(db, name, sha256)

    # Add Permission Node
    for p in permission:
        perNode = addPermissionNode(db, p)
        # Add their relationship
        createApkPerRelation(db, apkNode, perNode)
    print("Done for one APK: ",name)





if __name__ == '__main__':
    path = "C:/Users/caila/Desktop/FIT4003/APK Files/APK/Instagram.apk"
    address = "http://localhost:7474/db/data"
    username = "neo4j"
    password = "1234"
    db = connectDB(address, username, password)
    main(path,db)
  


    # theNode = addAPKNode(db,name,sha256)
    # APIL = readAPIText("C:/Users/caila/Desktop/FIT4003/androguard-master/Information/Info_API/Instagram.txt")
    # allCount = 0
    # errorCount = 0
    # for l in APIL:
    #     allCount += 1
    #     errorCount = createApkApiRelation(db, l, theNode, errorCount)
    # print(str(errorCount) + " / " + str(allCount))
    # print("All Done")

    