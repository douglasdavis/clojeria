#!/usr/bin/env python

import glob
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
import csv
import datetime
import pathlib

files = glob.glob("*.csv")


players = []
with open(files[0]) as f:
    for row in csv.DictReader(f):
        players.append(row["name"])

players = sorted(players)

pairs = []
for f in files:
    p = pathlib.PosixPath(f)
    d = datetime.date.fromisoformat(str(p.stem).replace(".", "-"))
    pairs.append((p, d))
pairs = sorted(pairs, key=lambda x: x[1])
dates = [p[1] for p in pairs]
banks = {p : [] for p in players}

for p, d in pairs:
    with open(p, "r") as csvf:
        for row in csv.reader(csvf):
            if row[0] == "name":
                continue
            banks[row[0]].append(float(row[1]))

print(banks)

banks.pop("jenny")
banks.pop("janet")
banks.pop("kevin")
banks.pop("alma")
banks.pop("juan")
banks.pop("shannon")

fig, ax = plt.subplots(figsize=(8,7))
for p in banks.keys():
    ax.plot(dates, banks[p], label=p.title())
ax.legend(loc="upper center", ncol=4)
ax.format_xdata  = mdates.DateFormatter("%m-%d")
fig.autofmt_xdate()
ax.set_title("Lotería", fontsize=14)
ax.set_ylabel("Bank Value ($)", fontsize=14)
ax.set_ylim([0, ax.get_ylim()[1]*1.3])
fig.savefig("banks.pdf", bbox_inches="tight")



# info = sorted(info, key=lambda x: x[1])
# print(info)
