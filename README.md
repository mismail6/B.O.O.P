# B.O.O.P
Button-Operated-Opening-Product

Goal is to create a system that allows me to push my physical "open main door" button virtually from my phone using an Android App with the help of a linear actuator, microprocessor, and Flask server.

Hardware:
1. Raspberry Pi 4B
2. L298N H bridge
3. Linear Actuator with built-in 12VDC motor (and its power supply)
4. Extras: Cables, mounted shelf, cardboard, charger.

Software:
1. Door-server.py
2. HomeButton App (Relevant files are MainActivity.Java, AndroidManifest.xml, Network_security_config.xml, activity_main.xml)

Note: I omitted or replaced code that showed my ip for security reasons.
Also, one thing to notice in the app code is that I use 2 URLs, one for public connection and one for local. I do this because sending requests locally through public IP was very slow and often stalled my linear actuator. So I had to make sure local requests are always sent through the local IP while external devices are routed through the public IP. And the same port is used for both.

Additional configurations:
Enabling port forwarding on the router that is connected to the Raspberry Pi.
Setting up Firewall rules on the Raspberry Pi iptables.

Here is what it looks like in action:
https://www.youtube.com/shorts/q7eWrnLUQiY

Feel free to email me at ismailjan12@gmail.com if you have any questions.
