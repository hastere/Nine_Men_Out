import csv
import numpy as np
import pandas as pd
from scipy import spatial

import requests
import simplejson
import json

def main():
    url = "https://therundown-therundown-v1.p.rapidapi.com/sports/4/events?include=scores+or+teams+or+all_periods"
    headers={"X-RapidAPI-Key": ""}
    responseInfo = requests.get(url,headers=headers)
    print("hello")
    bigDict = {}
    gameDict = {}
    number = 0
    responseJSON = json.loads(responseInfo.text)
    for game in responseJSON['events']:
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
        number = number + 1
        print(gameDict)
        bigDict[str(number)] = gameDict.copy()
        gameDict.clear()

    file_object = open("response.json", "w")
    file_object.write(simplejson.dumps(bigDict, indent=4, sort_keys=True))
    file_object.close()
    return 0




if __name__ == "__main__":
    main()
