#!/usr/bin/env python
"""Various methods reused in main scripts"""
import sys, os, subprocess, struct, numpy, math, multiprocessing

PC_FILE_FORMATS = ['las','laz']
OCTTREE_NODE_NUM_CHILDREN = 8

DB_NAME = 'pc_extents'
DB_TABLE_RAW = 'extent_raw'
DB_TABLE_POTREE = 'extent_potree'
DB_TABLE_POTREE_DIST = 'potree_dist'

def shellExecute(command, showOutErr = False):
    """ Execute the command in the SHELL and shows both stdout and stderr"""
    print command
    (out,err) = subprocess.Popen(command, shell = True, stdout=subprocess.PIPE, stderr=subprocess.PIPE).communicate()
    r = '\n'.join((out,err))
    if showOutErr:
        print r
    return r

# Check the LAStools is installed and that it is in PATH before libLAS
if shellExecute('lasinfo -version').count('LAStools') == 0:
    raise Exception("LAStools is not found!. Please check that it is in PATH and that it is before libLAS")

def getUserName():
    return os.popen('whoami').read().replace('\n','')

def getConnectString(dbName = None, userName= None, password = None, dbHost = None, dbPort = None, cline = False):
    """ Gets the connection string to be used by psycopg2 (if cline is False)
    or by psql (if cline is True)"""
    connString=''
    if cline:    
        if dbName != None and dbName != '':
            connString += " " + dbName
        if userName != None and userName != '':
            connString += " -U " + userName
        if password != None and password != '':
            os.environ['PGPASSWORD'] = password
        if dbHost != None and dbHost != '':
            connString += " -h " + dbHost
        if dbPort != None and dbPort != '':
            connString += " -p " + dbPort
    else:
        if dbName != None and dbName != '':
            connString += " dbname=" + dbName
        if userName != None and userName != '':
            connString += " user=" + userName
        if password != None and password != '':
            connString += " password=" + password
        if dbHost != None and dbHost != '':
            connString += " host=" + dbHost
        if dbPort != None and dbPort != '':
            connString += " port=" + dbPort
    return connString

def getPCFileDetails(absPath):
    """ Get the details (count numPoints and extent) of a LAS/LAZ file (using LAStools, hence it is fast)"""
    count = None
    (minX, minY, minZ, maxX, maxY, maxZ) = (None, None, None, None, None, None)
    (scaleX, scaleY, scaleZ) = (None, None, None)
    (offsetX, offsetY, offsetZ) = (None, None, None)

    command = 'lasinfo ' + absPath + ' -nc -nv -nco'
    for line in shellExecute(command).split('\n'):
        if line.count('min x y z:'):
            [minX, minY, minZ] = line.split(':')[-1].strip().split(' ')
            minX = float(minX)
            minY = float(minY)
            minZ = float(minZ)
        elif line.count('max x y z:'):
            [maxX, maxY, maxZ] = line.split(':')[-1].strip().split(' ')
            maxX = float(maxX)
            maxY = float(maxY)
            maxZ = float(maxZ)
        elif line.count('number of point records:'):
            count = int(line.split(':')[-1].strip())
        elif line.count('scale factor x y z:'):
            [scaleX, scaleY, scaleZ] = line.split(':')[-1].strip().split(' ')
            scaleX = float(scaleX)
            scaleY = float(scaleY)
            scaleZ = float(scaleZ)
        elif line.count('offset x y z:'):
            [offsetX, offsetY, offsetZ] = line.split(':')[-1].strip().split(' ')
            offsetX = float(offsetX)
            offsetY = float(offsetY)
            offsetZ = float(offsetZ)
    return (count, minX, minY, minZ, maxX, maxY, maxZ, scaleX, scaleY, scaleZ, offsetX, offsetY, offsetZ)

def getFileSize(absPath):
    """ Get the size of a file """
    try:
        if os.path.islink(absPath):
            return int(((os.popen('du -sm ' + os.readlink(absPath))).read().split('\t'))[0])
        else:
            return int(((os.popen('du -sm ' + absPath)).read().split('\t'))[0])
    except ValueError:
        return None