"""
This code was created as part of Jan Niklas Schäfer's bachelor thesis
Author: Jan Niklas Schäfer
"""
import numpy as np
import matplotlib.pyplot as plt

# hole detection _mcts

notch_hole_detection_mcts = 3050.215302
og_hole_detection_mcts = 2685.690581
overall_hole_detection_mcts = 2867.952942

remainig_time_notch_mcts = 49114.32
remainig_time_og_mcts = 49184.16
remaning_time_overall_mcts = 49149.24

kills_notch_mcts = 73.4
kills_og_mcts = 203.8
kills_overall_mcts = 277.2

mario_mode_notch_mcts = 0
mario_mode_og_mcts = 0
mario_mode_overall_mcts = 0

# average x positions a star with hole detection:

notch_aStar = 2901.159577
og_aStar = 2689.405896
overall_aStar = 2795.282736

remainig_time_notch_aStar = 51603.72
remainig_time_og_aStar = 50615.88
remaning_time_overall_aStar = 51109.8

kills_notch_aStar = 65
kills_og_aStar = 183.8
kills_overall_aStar = 248.8

mario_mode_notch_aStar = 0
mario_mode_og_aStar = 0
mario_mode_overall_aStar = 0

plt.tight_layout()


categories = ['RBA-HD', 'MCTS-HD']  # X-axis labels
notch_set_values = [notch_aStar, notch_hole_detection_mcts]  # Heights for "Notch Set"
og_ore_set_values = [og_aStar, og_hole_detection_mcts]  # Heights for "OG + Ore Set"
overall_values = [overall_aStar, overall_hole_detection_mcts]  # Heights for "Overall"

# X locations for bars
x = np.arange(len(categories))
width = 0.2  # Width of the bars

# Create the plot
fig, ax = plt.subplots(figsize=(10, 8), layout='constrained')

fontsize = 24
# Plot each group of bars
ax.bar(x - width, notch_set_values, width, label='RPN ', color='blue')
ax.bar(x, og_ore_set_values, width, label='Original + Ore', color='green')
ax.bar(x + width, overall_values, width, label='Overall', color='red')

# X-axis labels and title
ax.set_xlabel("Agents", fontsize=fontsize)
ax.set_ylabel("Values", fontsize=fontsize)
ax.tick_params(axis='y', labelsize=18)  # Change font size of y-axis numbers
ax.set_title("Average Final X Position of Agents", fontsize=fontsize)
ax.set_xticks(x)  # Set tick positions
ax.set_xticklabels(categories, fontsize=fontsize)  # Set tick labels

# Make legend larger
plt.legend(fontsize=20, loc='upper left', bbox_to_anchor=(1, 1))

# plt.savefig("bothComparedX.pdf")

# Show the plot
plt.show()
