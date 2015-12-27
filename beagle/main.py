

import requests, json, hashlib, random, time, sys; 


# curl -X POST \
#   -H "X-Parse-Application-Id: un3IO8XUAayEuT44Aq77sIuO9VAC0qDii6QqCsFY" \
#   -H "X-Parse-REST-API-Key: gbFriX5zMZPJQHgLbSImS3EO4BqTq3YeHgmMqKoo" \
#   -H "Content-Type: application/json" \
#   -d '{"score":1337,"playerName":"Sean Plott","cheatMode":false}' \
#   https://api.parse.com/1/classes/GameScore
# 

headers = {
  "X-Parse-Application-Id" : "un3IO8XUAayEuT44Aq77sIuO9VAC0qDii6QqCsFY",
  "X-Parse-REST-API-Key" : "gbFriX5zMZPJQHgLbSImS3EO4BqTq3YeHgmMqKoo",
  "Content-Type": "application/json"
}

urlUnlock =  "https://api.parse.com/1/classes/Unlock"

masterKey = "fgggggg"
code = "1234"

random.seed(); 



def sendChallenge(challenge): 
	url =  "https://api.parse.com/1/classes/Challenge/mFMlLIAtXH"
	

	data = { 'challenge': challenge, }
	print (requests.put(url, headers=headers, data=json.dumps(data)).text)

	return challenge

def generateResponse(challenge):
	
	original = code + ":" + masterKey + ":" + challenge
	m = hashlib.sha1();
	m.update(original)
	originalHashed = m.hexdigest()

	print(originalHashed)

	print("\n")
	return originalHashed


def generateChallenge(): 
	challenge = random.randint(0, sys.maxint)
	return str(challenge);


def unlockUsingChallenge(challenge):
	canBeUnlocked = False

	while not canBeUnlocked: 
	
		result = requests.get(urlUnlock, headers=headers)
		print(json.loads(result.text))
		response =  (json.loads(result.text)[u"results"][0][u"passkey"])

		challengeExncryped = generateResponse(challenge)
		print ('response '  + response)

		print ('challengeEncrypted '  + challengeExncryped)

	

		
	
		

		canBeUnlocked = challengeExncryped == response;

		if canBeUnlocked:
		  print("Can be opened")
		else:
		  print("Cannot Be opened")

		time.sleep(5)


unlockUsingChallenge( sendChallenge( generateChallenge()));
