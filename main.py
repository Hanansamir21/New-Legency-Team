import cv2
import numpy as np
import math
from collections import Counter
import xlsxwriter
def ImageProcessing( x ):
    img = cv2.imread(x)
    imgray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    x = np.mean(np.array(imgray) - int(np.mean(imgray)/18))
    ret, thresh = cv2.threshold(imgray, x, 255, 0)
    contours, hierarchy = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    mask = np.ones_like(img) * 255

    boxes = []
    wlist=[]
    hlist=[]
    xlist=[]
    
  for contour in contours:
        if cv2.contourArea(contour) > 100:
            hull = cv2.convexHull(contour)
            cv2.drawContours(mask, [hull], -1, 0, -1)
            x,y,w,h = cv2.boundingRect(contour)
            boxes.append([x,y,w,h])
            wlist.append([w])
            hlist.append([h])
            xlist.append([x])

    boxes = sorted(boxes, key=lambda box: box[0])
    Mh=np.amax(hlist)
    Mw=np.amax(wlist)
    Mx=np.amax(xlist)

    del boxes[0]
    
    result = cv2.imread("white.png")  
    for n,box in enumerate(boxes):
        x,y,w,h = box
        cv2.rectangle(result,(x,y),(x+w,y+(h)),(255,0,0),2)  
        
    width=[row[2] for row in boxes]
    b = Counter(width)
    most=b.most_common(1)
    d=most[0][0]

    workbook = xlsxwriter.Workbook('digitaled4.xlsx') 
    worksheet = workbook.add_worksheet("My sheet") 
    return 0
