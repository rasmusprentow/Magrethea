

import requests, json, hashlib; 


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


url =  "https://api.parse.com/1/classes/Unlock"

masterKey = "fgggggg"
code = "1234"

result = requests.get(url, headers=headers)
print(json.loads(result.text))
challenge =  (json.loads(result.text)[u"results"][0][u"passkey"])

print (challenge)

print("\n")

original = code + ":" + masterKey
m = hashlib.sha1();
m.update(original)

originalHashed = m.hexdigest()

print(originalHashed)

print("\n")

canBeUnlocked = originalHashed == challenge;

if canBeUnlocked:
  print("Can be opened")
else:
  print("Cannot Be opened")