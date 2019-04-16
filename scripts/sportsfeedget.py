import csv
import numpy as np
import pandas as pd
from scipy import spatial

import requests
import simplejson
import json
import time

def getNewGames():
    url = "https://therundown-therundown-v1.p.rapidapi.com/sports/4/events?include=scores+or+teams+or+all_periods"
    headers={"X-RapidAPI-Key": "b58d7149f0msh36091f7253015aep1aa685jsn204004568b2d"}
    responseInfo = requests.get(url,headers=headers)
    print("hello")
    bigDict = {}
    gameDict = {}
    responseJSON = json.loads(responseInfo.text)
    for game in responseJSON['events']:
        temp = game["event_date"]
        temp = time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime())
        gameDict['event_date'] = game['event_date']
        gameDict['event_id'] = game['event_id']
        gameDict['away_spread'] = game['lines']['4']['spread']['point_spread_away']
        gameDict['home_spread'] = game['lines']['4']['spread']['point_spread_home']
        gameDict['over_under'] = game['lines']['4']['total']['total_over']
        for team in game['teams']:
            if (str(team['is_away']) == "False"):
                gameDict['home_team'] = team['name']
            else:
                gameDict['away_team'] = team['name']
        print("\n")
        print(gameDict)
        bigDict[gameDict['event_id']] = gameDict.copy()
        gameDict.clear()


    file_object = open("response.json", "r")
    responseData = json.load(file_object)
    responseData.update(bigDict)
    file_object.close()
    file_object = open("response.json", "a")
    file_object.write(simplejson.dumps(responseData, indent=4, sort_keys=True))
    file_object.close()

    
    file_object = open("backup.json", "r")
    backupData = json.load(file_object)
    file_object.close()
    responseData.update(bigDict)
    file_object = open("backup.json", "a")
    file_object.write(simplejson.dumps(backupData, indent=4, sort_keys=True))
    file_object.close()
    return 0

def updateGames():
    urlStart = "https://therundown-therundown-v1.p.rapidapi.com/events/"
    urlEnd = "?include=scores"
    headers={"X-RapidAPI-Key": "b58d7149f0msh36091f7253015aep1aa685jsn204004568b2d"}

    file_object = open("backup.json", "r")
    data = json.load(file_object)
    updatedGames = {}

    for item in data:
        if "score_away" not in data[item]:
            print(data[item]["event_id"])
            print("not updated")
            url = urlStart + data[item]["event_id"] + urlEnd
            responseInfo = requests.get(url,headers=headers)
            responseJSON = json.loads(responseInfo.text)
            print(responseJSON["score"]["event_status"])
            if responseJSON["score"]["event_status"] == "STATUS_FINAL":
                print("game over")
                print(responseJSON["score"]["score_away"])
                data[item]["score_away"] = responseJSON["score"]["score_away"]
                data[item]["score_home"] = responseJSON["score"]["score_home"]
                if responseJSON["score"]["winner_away"] == 1:
                    data[item]["winner"] = "away"
                else:
                    data[item]["winner"] = "home"
                updatedGames[item] = data[item].copy()
    file_object.close()
    file_object = open("backup.json", "w")
    file_object.write(simplejson.dumps(data, indent=4, sort_keys=True))
    file_object.close()
    file_object = open("response.json", "w")
    file_object.write(simplejson.dumps(updatedGames, indent=4, sort_keys=True))
    file_object.close()

    return 0

def main():

    updateGames()
    getNewGames()


    return 0




if __name__ == "__main__":
    main()
