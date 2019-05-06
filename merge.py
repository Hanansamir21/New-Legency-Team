import BtestDash
from flask import Flask
from flask import request
app = Flask(__name__)
@app.route('/ReceiveImage', methods = ['POST'])
def ReceiveImage():
    print("Connected")  #connected to the app                 
    content = request.form['name']  #receive image in base64 string form
    with open("imageToSave.png", "wb") as fh: 
        fh.write(content.decode('base64')) #convert base64 string to the image again
    BtestDash.ImageProcessing("imageToSave.png")#doing the image processing and extract excel file
    print("Excel is created")
    return 'Done'
app.run(host='0.0.0.0', port= 5000)
