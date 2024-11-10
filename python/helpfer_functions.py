import csv
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error
import pickle


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


def get_lift_event_history(current_time, own_df=None, time_window_in_min=15, filename="Export03Mar24.csv"):
    if own_df is None:
        df = pd.read_csv(
            f'data/skiing_data/Daten Bergbahnen SFL - Tourism Technology 24/Frequenzdaten SFL Winter 2023-24/{filename}',
            encoding='latin-1', delimiter=';')
    else:
        df = own_df

    end_time = current_time - np.timedelta64(time_window_in_min, 'm')

    lift_util = {}
    for lift in df.Lift.unique():
        lift_df = df.loc[df.Lift == lift]
        lift_df.SteckDatumZeit = pd.to_datetime(lift_df.SteckDatumZeit)
        count = lift_df.SteckDatumZeit.between(end_time, current_time).sum()
        lift_util[lift] = float(count)

    return lift_util


def predict_future_timestamp(req_timestamp: str):
    with open('data/my_dict.pickle', 'rb') as handle:
        test_dict = pickle.load(handle)

    df = pd.DataFrame(test_dict).T
    df['timestamp'] = pd.to_datetime(df.index)

    # Feature Engineering
    df['hour'] = df['timestamp'].dt.hour
    df['minute'] = df['timestamp'].dt.minute

    predictions = {}

    for lift in df.columns:
        try:
            X = df[['hour', 'minute']]
            y = df[lift]

            # Train-Test Split
            X_train, X_test, y_train, y_test = train_test_split(X.values, y.values, test_size=0.2, random_state=42)

            # Model Selection
            model = RandomForestRegressor(n_estimators=100, random_state=42)

            # Training the Model
            model.fit(X_train, y_train)

            # Prediction
            y_pred = model.predict(X_test)

            # Evaluation
            mse = mean_squared_error(y_test, y_pred)
            print(f'Mean Squared Error: {mse}')

            # Predicting future timestamp
            future_timestamp = pd.to_datetime(req_timestamp)
            future_features = np.array([[future_timestamp.hour, future_timestamp.minute]])
            future_prediction = model.predict(future_features)

            predictions[lift] = future_prediction[0]

        except Exception as e:
            print(f"{lift} - Error: {e}")
    return predictions
