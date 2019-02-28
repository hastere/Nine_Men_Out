import csv
import numpy as np
import pandas as pd
from scipy import spatial

import requests
import simplejson

def main():
    url = "https://therundown-therundown-v1.p.rapidapi.com/sports/4/events?include=scores+or+teams+or+all_periods"
    headers={"X-RapidAPI-Key": "b58d7149f0msh36091f7253015aep1aa685jsn204004568b2d"}
    response = requests.get(url,headers=headers)
    print("hello")
    file_object = open("response.json", "w")
    file_object.write(simplejson.dumps(simplejson.loads(response.text), indent=4, sort_keys=True))
    file_object.close()
    return 0




if __name__ == "__main__":
    main()
