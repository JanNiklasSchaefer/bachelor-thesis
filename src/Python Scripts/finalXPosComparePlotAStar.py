"""
This code was created as part of Jan Niklas Schäfer's bachelor thesis
Author: Jan Niklas Schäfer
"""
import numpy as np
import matplotlib.pyplot as plt

# average x positions a star no hole detection:

notch = 2562.908512
og = 2527.80392
overall = 2545.356216

remainig_time_notch = 52564.44
remainig_time_og = 51820.8
remaning_time_overall = 52192.62

kills_notch = 64.2
kills_og = 172.6
kills_overall = 236.8

mario_mode_notch = 0
mario_mode_og = 0
mario_mode_overall = 0

# average x positions a star with hole detection:

notch = 2901.159577
og = 2689.405896
overall = 2795.282736

remainig_time_notch = 51603.72
remainig_time_og = 50615.88
remaning_time_overall = 51109.8

kills_notch = 65
kills_og = 183.8
kills_overall = 248.8

mario_mode_notch = 0
mario_mode_og = 0
mario_mode_overall = 0

# Example data (Replace these values with your actual data)
categories = ['RBA-Orig', 'RBA-HD']  # X-axis labels
notch_set_values = [2562.908512, 2901.159577]  # Heights for "Notch Set"
og_ore_set_values = [2527.80392, 2689.405896]  # Heights for "OG + Ore Set"
overall_values = [2545.356216, 2795.282736]  # Heights for "Overall"

# X locations for bars
x = np.arange(len(categories))
width = 0.2  # Width of the bars

# Create the plot
fig, ax = plt.subplots(figsize=(10, 10), layout='constrained')

fontsize = 24
# Plot each group of bars
ax.bar(x - width, notch_set_values, width, label='NPR', color='blue')
ax.bar(x, og_ore_set_values, width, label='Original + Ore', color='green')
ax.bar(x + width, overall_values, width, label='Overall', color='red')

# X-axis labels and title
ax.set_xlabel("Agents", fontsize=fontsize)
ax.set_ylabel("Values", fontsize=fontsize)
ax.tick_params(axis='y', labelsize=18)  # Change font size of y-axis numbers
ax.set_ylim(0, 3000)
ax.set_title("Average Final X Position of A* Agents", fontsize=fontsize)
ax.set_xticks(x)  # Set tick positions
ax.set_xticklabels(categories, fontsize=fontsize)  # Set tick labels

plt.legend(fontsize=20, loc='upper left', bbox_to_anchor=(1, 1))

#plt.savefig("aStarComparedX.pdf")

# Show the plot
plt.show()
