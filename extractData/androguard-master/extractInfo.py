"""
Author: Chai Lam Loi
Last Modified : 2019-8-17
Aim: Use AndroGuard's provided function to extract the required information of Android apk:  
Permissions; APIs; Services; Providers; Packages; Activities; Receivers; APK Size; Target SDK Version; Min SDK Version; APK Name
Write the information extracted to a txt file for one apk.
Reference: https://www.jianshu.com/p/670023af50f6
Required: Check the Path of APK and filename to write to, before started.
"""

#coding=utf-8
from androguard.misc import AnalyzeAPK
from androguard.session import Session
import hashlib
import os


def getFullApi(jsonOutput, a, d, dx):
  """
  All methods that are external are the API calls.
  getApi does not use the outputFile function.
  """
  print("Getting All APIs....")
  f = open(filename + a.get_app_name()+'.txt', 'w')
  f.write("\n")
  f.write("\n")
  f.write("All APIs: ")
  f.write("\n")
  externalClass = dx.get_external_classes()
  for i in externalClass:
    f.write(i.get_vm_class().get_name().__repr__())
    f.write("\t")
  f.close()
  print("Done All APIs")


  
def getProvider(jsonOutput, a, d, dx):
  print("Getting Providers....")
  providers = a.get_providers()
  #print (providers)
  string = "Providers:"
  if type(providers) is list:
    file = providers
  else:
    file = [providers]
  outputFile(string, file, filename, a)
  print("Done Providers")
  return providers


def getPackage(jsonOutput, a, d, dx):
  print("Getting Packages....")
  packname = a.get_package()
  #print (packname)
  string = "PackageName:"
  if type(packname) is list:
    file = packname
  else:
    file = [packname]
  outputFile(string, file, filename, a)
  print("Done Packages")
  return packname


def getActivity(jsonOutput, a, d, dx):
  print("Getting Activities....")
  activitys = a.get_activities()
  #print (activitys)
  string = "Activitys:"
  if type(activitys) is list:
    file = activitys
  else:
    file = [activitys]
  outputFile(string, file, filename, a)
  print("Done Activities")
  return activitys


def getReceiver(jsonOutput, a, d, dx):
  print("Getting Receivers....")
  receivers = a.get_receivers()
  #print (receivers)
  string = "Receivers:"
  if type(receivers) is list:
    file = receivers
  else:
    file = [receivers]
  outputFile(string, file, filename, a)
  print("Done Receivers")
  return receivers


def getService(jsonOutput, a, d, dx):
  print("Getting Services....")
  services = a.get_services()
  #print (services)
  string = "Services:"
  if type(services) is list:
    file = services
  else:
    file = [services]
  outputFile(string, file, filename, a)
  print("Done Services")
  return services


def getTargetSDKVersion(jsonOutput, a, d, dx):
  print("Getting Target SDK Version....")
  tVersion = a.get_target_sdk_version()
  string = "Target SDK Version:"
  if type(tVersion) is list:
    file = tVersion
  else:
    file = [tVersion]
  outputFile(string, file, filename, a)
  print("Done Target SDK Version")
  return tVersion


#--------------------------------------------------------------#

def getAppName(a, d, dx):
  print("Getting Apk Name....")
  string = "name"
  name = a.get_app_name()
  print("Done Name")
  return string, str(name)


def getAPKSHA256(path):
  print("Getting SHA256 for APK....")
  string = "SHA256"
  with open(path, "rb") as fp:
        raw_data = fp.read()
  sha256 = hashlib.sha256(raw_data).hexdigest()
  fp.close()
  print("Done SHA256 for APK")
  return string, str(sha256)


def getVersionCode(a, d, dx):
  """
  The version code is an incremental integer value that represents the version of the application code. 
  """
  print("Getting Version Code....")
  string = "versionCode"
  versionCode = a.get_androidversion_code()
  print("Done Version Code")
  return string, str(versionCode)


def getVersionName(a, d, dx):
  """
  The version name is a string value that represents the “friendly” version name displayed to the users.
  """
  print("Getting Version Name....")
  string = "versionName"
  versionName = a.get_androidversion_name()
  print("Done Version Name")
  return string, str(versionName)


def getAPKSize(path, a, d, dx):
  """
  Return size in bytes
  """
  sizeAPK = os.path.getsize(path)
  string = "size"
  return string, str(sizeAPK)


def getMinSDKVersion(a, d, dx):
  print("Getting Minimum SDK Version....")
  mVersion = a.get_min_sdk_version()
  string = "minSDKVersion"
  print("Done Minimum SDK Version")
  return string, str(mVersion)


def getSHA256Certificate(a, d, dx):
  print("Getting SHA256 Certificate....")
  cert = a.get_certificates()
  string = "certificate(fingerprint)"
  for c in cert:
    sha256 = c.sha256_fingerprint
  sha256 =sha256.replace(" ",":")
  print("Done SHA256 Certificate")
  return string, str(sha256)


def getCertOwner(a, d, dx):
  print("Getting Certificate Owner....")
  cert = a.get_certificates()
  string = "certificate(owner)"
  for c in cert:
    owner = c.issuer.human_friendly
  print("Done Certificate Owner")
  return string, str(owner)


def getPermission(a, d, dx):
  print("Getting Permissions....")
  string = "permission"
  permission = a.get_permissions()
  if type(permission) is list:
    per = permission
  else:
    per = [permission]
  #print (permission)
  print("Done Permissions")
  return string, per


def argExtract(descriptor):
  """
  Do some house keeping for the argument. Extract and filter it.
  the descriptor returned of the method A method descriptor will have the form (A A A …)R 
  Where A are the arguments to the method and R is the return type. 
  Basic types will have the short form, i.e. I for integer, V for void 
  and class types will be named like a classname, e.g. Ljava/lang/String
  More information about type descriptors are found here: 
  https://source.android.com/devices/tech/dalvik/dex-format#typedescriptor
  """
  args,ret = descriptor[1:].split(")") # argument and return
  args = args.replace(";","")
  if len(args) > 0:
        args = args.split(" ")
  argName = ""
  for j in range (0,len(args)):
    args[j] = args[j].replace("$",".")
    if len(args[j]) > 0:
      if args[j][0] == "L":
        args[j] = args[j][1:] # remove the L letter for argument, eg Ljava.object.xxxx
    # Rename the TypeDescriptor to proper type
    if len(args[j]) == 1 or len(args[j]) == 2:
      if args[j] == "J" or "[J":
        args[j] = "long"
      elif args[j] == "I" or "[I":
        args[j] = "int"
      elif args[j] == "F" or "[F":
        args[j] = "float"
      elif args[j] == "Z" or "[Z":
        args[j] = "boolean"
      elif args[j] == "V" or "[V":
        args[j] = "void"
      elif args[j] == "C" or "[C":
        args[j] = "char"
      elif args[j] == "B" or "[B":
        args[j] = "byte"  
      elif args[j] == "S" or "[S":
        args[j] = "short"
      elif args[j] == "D" or "[D":
        args[j] = "double"

    argName = str(args[j]) + "," + argName  
  argName = argName[:-1]
  return argName



def getAndroidApi(a, d, dx):
   print("Getting Android APIs....")
   string = "API"
   apiList = []
   nameAPK = a.get_app_name()
   nameAPK = nameAPK.replace("?",'')
   nameAPK = nameAPK.replace("/",'')

   for method in dx.get_methods():
     # If it is android api
     if method.is_android_api():
       argName = argExtract(method.method.get_descriptor())
       className = method.method.class_name
       className = className.replace(";","")
       className = className.replace("$",".")
       className = className[1:] # remove the L letter for class and package name, eg Ljava.object.xxx
       methodName = method.method.get_name()

       if className[0:7] == "android" or className[0:4] == "java":
        apiName = className + "." + methodName + "(" + argName + ")"
        apiName = apiName.replace("/",".")
      
        #try:
        apiName.replace(" ","")
        apiList.append(str(apiName))
        #except:
        #  print("exception")

   print("Done Android APIs")
   return string, apiList


def outputFile(string, filename, a):
    nameAPK = a.get_app_name()
    nameAPK = nameAPK.replace("?",'')
    nameAPK = nameAPK.replace("/",'')
    print(nameAPK)
    f = open(filename + nameAPK+'.txt', 'w')
    string = string.replace(u'\xa0', u' ')

    try:
      f.write(string)
    except:
      print("Write error")
    f.close()


def toOutputFile(path, filename):
  # a = apk, d = dalvikVMFormat, dx = analysis
  print("Analyzing APK....")
  a, d, dx = AnalyzeAPK(path)
  jsonOutput = "{\n"

  #--------------JSON Format---------------#
  # Order of JSON format is name, SHA256 hash, versionCode, versionName, size, minSDKVersion, certificate(fingerprint), certificate(owner), permission
  
  string, result = getAppName( a, d, dx)
  tmp = '"' + string + '":"' + result + '", \n'
  jsonOutput = jsonOutput + tmp

  string, result = getAPKSHA256(path)
  tmp = '"' + string + '":"' + result + '", \n'
  jsonOutput = jsonOutput + tmp

  string, result = getVersionCode(a, d, dx)
  tmp = '"' + string + '":"' + result + '", \n'
  jsonOutput = jsonOutput + tmp

  string, result = getVersionName(a, d, dx)
  tmp = '"' + string + '":"' + result + '", \n'
  jsonOutput = jsonOutput + tmp
  
  string, result = getAPKSize(path, a, d, dx)
  tmp = '"' + string + '":' + result + ', \n'
  jsonOutput = jsonOutput + tmp

  string, result = getMinSDKVersion(a, d, dx)
  tmp = '"' + string + '":"' + result + '", \n'
  jsonOutput = jsonOutput + tmp

  string, result = getSHA256Certificate(a, d, dx)
  tmp = '"' + string + '":"' + result + '", \n'
  jsonOutput = jsonOutput + tmp

  string, result = getCertOwner(a, d, dx)
  tmp = '"' + string + '":"' + result + '", \n'
  jsonOutput = jsonOutput + tmp

  string, result = getPermission(a, d, dx)
  result = str(result)
  result = result.replace("'",'"')
  tmp = '"' + string + '":' + result + ', \n'
  jsonOutput = jsonOutput + tmp

  string, result = getAndroidApi(a, d, dx)
  result = str(result) 
  result = result.replace("'",'"')
  tmp = '"' + string + '":' + result + '\n}'
  jsonOutput = jsonOutput + tmp


  #print(jsonOutput)
  outputFile(jsonOutput, filename, a)

#-------------------To Be Considered---------------#
  #getFullApi(filename, a, d, dx)
  #getProvider(filename, a, d, dx)
  #getPackage(filename, a, d, dx)
  #getActivity(filename, a, d, dx)
  #getReceiver(filename, a, d, dx)
  #getService(filename, a, d, dx)
  #getTargetSDKVersion(filename, a, d, dx)


def toOutputNode(path):
  # Currently only contain APK (name & sha256), Permission and API Node.
  print("Analyzing APK....")
  a, d, dx = AnalyzeAPK(path)
  string, name = getAppName( a, d, dx)
  string, sha256 = getAPKSHA256(path)
  string, permission = getPermission(a, d, dx)
  print("Done Analyze.")

  return name, sha256, permission


if __name__ == '__main__':
    path = "C:/Users/caila/Desktop/FIT4003/APK Files/APK/Sims3.apk"
    filename = "aaaa/"
    toOutputFile(path, filename)
