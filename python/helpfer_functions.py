import csv
import pandas as pd
import numpy as np


def read_csv_return_json(csvFilePath):
    # create a dictionary
    data = []

    # Open a csv reader called DictReader
    with open(csvFilePath, encoding='utf-8-sig') as csvf:
        csvReader = csv.DictReader(csvf)

        # Convert each row into a dictionary
        # and add it to data
        for rows in csvReader:
            data.append(rows)

    # Open a json writer, and use the json.dumps()
    # function to dump data
    return data


def get_foerder_kappa_all():
    fkappa_df = pd.read_excel("data/Kurzbeschreibung und Anlagenzuordnung.xlsx")
    foerder_dict = {}

    for i, row in fkappa_df.iterrows():
        foerder_dict[row.Anlage] = {"theoretical_foerder_kapa": row.theor_foerder_kapa_h,
                                    "practical_foerder_kapa": row.practical_foerder_kapa_h}

    return foerder_dict


def get_foerder_kappa_specific(lift_name: str):
    fkappa_df = pd.read_excel("data/Kurzbeschreibung und Anlagenzuordnung.xlsx")
    item = fkappa_df.loc[fkappa_df.Anlage == lift_name]
    return {item['Anlage'].values[0]: {"theoretical_foerder_kapa": float(item.theor_foerder_kapa_h.values[0]),
                                       "practical_foerder_kapa": float(item.practical_foerder_kapa_h.values[0])}}


def get_lift_event_history(current_time, time_window_in_min=15):
    df = pd.read_csv('data/Export03Mar24.csv', encoding='latin-1', delimiter=';')
    current_time = pd.to_datetime(current_time)
    end_time = current_time - np.timedelta64(time_window_in_min, 'm')

    lift_util = {}
    for lift in df.Lift.unique():
        lift_df = df.loc[df.Lift == lift]
        lift_df.SteckDatumZeit = pd.to_datetime(lift_df.SteckDatumZeit)
        count = lift_df.SteckDatumZeit.between(end_time, current_time).sum()
        lift_util[lift] = float(count)

    return lift_util
