from flask import Flask, request
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
import RPi.GPIO as GPIO          
import time
from threading import Lock

in1 = 24
in2 = 23
en = 25
temp1=1

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(in1,GPIO.OUT)
GPIO.setup(in2,GPIO.OUT)
GPIO.setup(en,GPIO.OUT)
GPIO.output(in1,GPIO.LOW)
GPIO.output(in2,GPIO.LOW)
p=GPIO.PWM(en,1000)
p.start(50) 

app = Flask(__name__)
limiter = Limiter(app, default_limits=["3 per minute"])
lock = Lock()

SECRET_TOKEN = "Your own secret token here"

@app.route('/trigger-actuatorGRAGAS', methods=['POST'])
def trigger_actuatorGRAGAS():
    token = request.headers.get('Token')  
    if token == SECRET_TOKEN:
        with lock:
            GPIO.output(in1, GPIO.LOW)
            GPIO.output(in2, GPIO.HIGH)
            time.sleep(2.2)
            GPIO.output(in1, GPIO.LOW)
            GPIO.output(in2, GPIO.LOW)
            time.sleep(1.5)
            GPIO.output(in1, GPIO.HIGH)
            GPIO.output(in2, GPIO.LOW)
            time.sleep(3)
            GPIO.output(in1, GPIO.LOW)
        return 'Actuator triggered'
    else:
        return 'Unauthorized', 401
    
    return 'Actuator triggered'
    

if __name__ == '__main__':
    app.run(host='0.0.0.0', port="Enter port here")