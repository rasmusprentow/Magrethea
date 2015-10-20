#!/bin/bash

# 
# curl -X POST \
#   -H "X-Parse-Application-Id: un3IO8XUAayEuT44Aq77sIuO9VAC0qDii6QqCsFY" \
#   -H "X-Parse-REST-API-Key: gbFriX5zMZPJQHgLbSImS3EO4BqTq3YeHgmMqKoo" \
#   -H "Content-Type: application/json" \
#   -d '{"score":1337,"playerName":"Sean Plott","cheatMode":false}' \
#   https://api.parse.com/1/classes/GameScore


curl -X GET \
  -H "X-Parse-Application-Id: un3IO8XUAayEuT44Aq77sIuO9VAC0qDii6QqCsFY" \
  -H "X-Parse-REST-API-Key: gbFriX5zMZPJQHgLbSImS3EO4BqTq3YeHgmMqKoo" \
  -H "Content-Type: application/json" \
  https://api.parse.com/1/classes/GameScore

