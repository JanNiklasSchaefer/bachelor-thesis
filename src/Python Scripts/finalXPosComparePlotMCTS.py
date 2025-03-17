"""
This code was created as part of Jan Niklas Schäfer's bachelor thesis
Author: Jan Niklas Schäfer
"""
import numpy as np
import matplotlib.pyplot as plt

# vanilla

notch_vanilla = 2896.419166
og_vanilla = 2495.104243
overall_vanilla = 2695.761705

remainig_time_notch = 49249.92
remainig_time_og = 50009.52
remaning_time_overall = 49629.72

kills_notch = 64.6
kills_og = 193.8
kills_overall = 258.4

mario_mode_notch = 0
mario_mode_og = 0
mario_mode_overall = 0

# hole detection

notch_hole_detection = 3050.215302
og_hole_detection = 2685.690581
overall_hole_detection = 2867.952942

remainig_time_notch = 49114.32
remainig_time_og = 49184.16
remaning_time_overall = 49149.24

kills_notch = 73.4
kills_og = 203.8
kills_overall = 277.2

mario_mode_notch = 0
mario_mode_og = 0
mario_mode_overall = 0

# mixMax

notch_mix_max = 2825.880692
og_mix_max = 2434.44897
overall_mix_max = 2630.164831

# partial + roulette

notch_partial_roulette = 529.8314262
og_partial_roulette = 183.2150531
overall_partial_roulette = 356.5232397

# monteMario

notch_monteMario = 423.6708135
og_monteMario = 164.961035
overall_monteMario = 294.3159242

# mixMaxHoleDetection

notch = 3057.062495
og = 2614.315053
overall = 2835.688774


categories = ['Vanilla', 'HD', 'MixMax', 'PSRW', 'Monte']  # X-axis labels
notch_set_values = [notch_vanilla, notch_hole_detection, notch_mix_max, notch_partial_roulette,
                    notch_monteMario]  # Heights for "Notch Set"
og_ore_set_values = [og_vanilla, og_hole_detection, og_mix_max, og_partial_roulette,
                     og_monteMario]  # Heights for "OG + Ore Set"
overall_values = [overall_vanilla, overall_hole_detection, overall_mix_max, overall_partial_roulette,
                  overall_monteMario]  # Heights for "Overall"

# X locations for bars
x = np.arange(len(categories))
width = 0.2  # Width of the bars

# Create the plot
fig, ax = plt.subplots(figsize=(13, 11), layout='constrained')

fontsize = 26
# Plot each group of bars
ax.bar(x - width, notch_set_values, width, label='Notch', color='blue')
ax.bar(x, og_ore_set_values, width, label='Original + Ore', color='green')
ax.bar(x + width, overall_values, width, label='Overall', color='red')

# X-axis labels and title
ax.set_xlabel("Agents", fontsize=fontsize)
ax.set_ylabel("Value", fontsize=fontsize)
ax.tick_params(axis='y', labelsize=22)  # Change font size of y-axis numbers
ax.set_title("Average Final X Position of MCTS Agents", fontsize=fontsize)
ax.set_xticks(x)  # Set tick positions
ax.set_xticklabels(categories, fontsize=fontsize)  # Set tick labels

plt.legend(fontsize=20, loc='upper left', bbox_to_anchor=(1, 1))

#plt.savefig("mctsComparedX.pdf")

# Show the plot
plt.show()
