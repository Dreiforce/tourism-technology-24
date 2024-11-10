from typing import Union

import numpy as np
import uvicorn
from fastapi import FastAPI

from helpfer_functions import read_csv_return_json, get_foerder_kappa_all, get_foerder_kappa_specific, \
    get_lift_event_history, predict_future_timestamp, get_lift_names

app = FastAPI()


@app.get("/serffaus/arrivalDeparture")
def read_arrival_departure():
    return read_csv_return_json(
        "data/2024-03-03-Serffaus/Serfaus-Fiss-Ladis - 2024-03-03 (6 - 21h) - Arriving and Departing Visitors.csv")

@app.get("/serffaus/socialDemographics")
def read_social_demographics():
    return read_csv_return_json("data/2024-03-03-Serffaus/Serfaus-Fiss-Ladis - 2024-03-03 (6 - 21h) - Social Demographics.csv")

@app.get("/serffaus/topCountries")
def read_top_countries():
    return read_csv_return_json(
        "data/2024-03-03-Serffaus/Serfaus-Fiss-Ladis - 2024-03-03 (6 - 21h) - Top Countries of Origin.csv")

@app.get("/serffaus/dailyVisitors")
def read_root():
    return read_csv_return_json(
        "data/2024-03-03-Serffaus/Serfaus-Fiss-Ladis - 2024-03-03 (6 - 21h) - Visitors During the Day.csv")

@app.get("/foerder/kapaAll")
def read_kapa_all():
    return get_foerder_kappa_all()

@app.get("/foerder/kapaLift")
def read_kapa_lift(lift: str):
    return get_foerder_kappa_specific(lift)

@app.get("/lift")
def lift_events():
    return get_lift_names()

@app.get("/lift/events")
def lift_events(current_time: str, time_window=15):
    current_time = np.datetime64(current_time)
    return get_lift_event_history(current_time, time_window_in_min=time_window)

@app.get("/predict/liftKapa")
def predict_lift_kapa(timestamp: str):
    return predict_future_timestamp(timestamp)


@app.get("/lift/targets")
def lift_events():
    return [
        {"from":"FI Schönjochbahn Talst.", "to": "FI Schönjoch Mitte Bergfahrt", "rate": 2600},
        {"from":"FI Schönjoch Mitte Bergfahrt", "to": "FI Schönjoch Mitte Talfahrt", "rate": 2600},
        # {"from":"FI Schönjoch Mitte Talfahrt", "to": "FI Schönjochbahn Berg", "rate": 2600},
        # TODO: add rest of dataset
    ]



if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
