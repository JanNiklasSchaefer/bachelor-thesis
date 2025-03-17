"""
This code was created as part of Jan Niklas Schäfer's bachelor thesis
Author: Jan Niklas Schäfer
"""
import numpy as np
import matplotlib.pyplot as plt

speed_increase = [0, 1.0680001, 0.95052004, 0.84596276, 0.75290656, 0.6700871, 0.5963774, 0.530776, 0.47239065,
                  0.4204278, 0.37418032, 0.33302116, 0.29638863, 0.26378584, 0.23476887, 0.20894432, 0.18596077,
                  0.16550446, 0.14729977, 0.13109684, 0.11667538, 0.10384178, 0.09241867, 0.0822525, 0.073204994,
                  0.06515217, 0.057985306, 0.051607132, 0.045930862, 0.040878296, 0.03638172, 0.03237915, 0.02881813,
                  0.025648117, 0.022826195, 0.020316124, 0.018080711, 0.0160923, 0.014322281, 0.012746811, 0.011343956,
                  0.01009655, 0.008985519, 0.007997513, 0.0071172714, 0.0063352585, 0.0056381226, 0.0050172806,
                  0.004466057, 0.0039749146, 0.003537178, 0.003148079, 0.0028018951, 0.0024938583, 0.0022192001,
                  0.0019750595, 0.0017576218, 0.0015649796, 0.0013923645, 0.0012397766, 0.0011034012, 9.813309E-4,
                  8.735657E-4, 7.7724457E-4, 6.9236755E-4, 6.160736E-4, 5.4836273E-4, 4.8828125E-4, 4.339218E-4,
                  3.862381E-4, 3.4427643E-4, 3.0612946E-4, 2.7275085E-4, 2.4223328E-4, 2.155304E-4, 1.9168854E-4,
                  1.707077E-4, 1.5258789E-4, 1.3542175E-4, 1.20162964E-4, 1.0681152E-4, 9.536743E-5, 8.4877014E-5,
                  7.534027E-5, 6.771088E-5, 6.0081482E-5, 5.340576E-5, 4.7683716E-5, 4.196167E-5, 3.71933E-5,
                  3.33786E-5,
                  2.9563904E-5, 2.670288E-5, 2.3841858E-5, 2.0980835E-5, 1.9073486E-5, 1.6212463E-5, 1.5258789E-5,
                  1.335144E-5, 1.1444092E-5, 1.04904175E-5, 9.536743E-6, 8.583069E-6, 7.6293945E-6, 6.67572E-6,
                  5.722046E-6, 4.7683716E-6, 4.7683716E-6, 3.8146973E-6, 3.8146973E-6, 2.861023E-6, 2.861023E-6,
                  2.861023E-6, 1.9073486E-6, 1.9073486E-6, 1.9073486E-6, 1.9073486E-6, 9.536743E-7, 9.536743E-7,
                  9.536743E-7, 9.536743E-7, 9.536743E-7, 9.536743E-7, 9.536743E-7, 9.536743E-7, 9.536743E-7,
                  9.536743E-7]

forward_movement_with_sprinting = [1.1999998, 2.2680006, 3.2185202, 4.0644827, 4.8173904, 5.4874763, 6.0838547,
                                   6.6146317, 7.087021, 7.5074463, 7.88163, 8.214653, 8.51104, 8.774826, 9.00959,
                                   9.218536, 9.404495, 9.57, 9.7173, 9.848404, 9.965073, 10.068924, 10.161331,
                                   10.243591, 10.316788, 10.381943, 10.439926, 10.491531, 10.53746, 10.578339,
                                   10.614716, 10.647095, 10.675934, 10.701569, 10.724396, 10.74472, 10.762787,
                                   10.7789, 10.793213, 10.805969, 10.817291, 10.827393, 10.836395, 10.844391,
                                   10.851501, 10.857849, 10.863464, 10.8685, 10.872955, 10.876923, 10.880463,
                                   10.883606, 10.886414, 10.888916, 10.891113, 10.893127, 10.894836, 10.896423,
                                   10.897827, 10.899048, 10.9001465, 10.901123, 10.902039, 10.902771, 10.903503,
                                   10.904114, 10.904663, 10.905151, 10.905579, 10.905945, 10.906311, 10.906616,
                                   10.90686, 10.9071045, 10.907349, 10.907532, 10.907715, 10.907837, 10.90802,
                                   10.908142, 10.908203, 10.908325, 10.908386, 10.908447, 10.908569, 10.90863,
                                   10.90863, 10.908691, 10.908752, 10.908813, 10.908813, 10.9088745, 10.9088745,
                                   10.9088745, 10.908936, 10.908936, 10.908936, 10.908997, 10.908997, 10.908997,
                                   10.908997, 10.909119, 10.909058, 10.909058, 10.909058, 10.909058, 10.909058,
                                   10.909058, 10.909058, 10.909058, 10.909058, 10.909058, 10.909058, 10.909058,
                                   10.909058, 10.909058, 10.909058, 10.909058, 10.909058, 10.909058, 10.909058,
                                   10.909058, 10.909058, 10.909058, 10.909058, 10.909058, 10.909058]

current_speed = [0.0, 1.0680001, 2.01852, 2.8644829, 3.6173894, 4.2874765, 4.883854, 5.41463, 5.8870206, 6.3074484,
                 6.6816287, 7.01465, 7.3110385, 7.5748243, 7.809593, 8.0185375, 8.204498, 8.370003, 8.5173025, 8.648399,
                 8.765075, 8.8689165, 8.961335, 9.043588, 9.116793, 9.181945, 9.23993, 9.291537, 9.337468, 9.378346,
                 9.414728, 9.447107, 9.475925, 9.501574, 9.5244, 9.544716, 9.562797, 9.578889, 9.593211, 9.605958,
                 9.617302, 9.6273985, 9.636384, 9.6443815, 9.651499, 9.657834, 9.663472, 9.668489, 9.6729555, 9.67693,
                 9.680468, 9.683616, 9.686418, 9.688911, 9.691131, 9.693106, 9.694863, 9.696428, 9.697821, 9.69906,
                 9.700164, 9.701145, 9.702019, 9.702796, 9.703488, 9.704104, 9.704653, 9.705141, 9.705575, 9.705961,
                 9.7063055, 9.706612, 9.706884, 9.707127, 9.707342, 9.707534, 9.707705, 9.707857, 9.707993, 9.708113,
                 9.70822, 9.708315, 9.7084, 9.708475, 9.708543, 9.708603, 9.708656, 9.708704, 9.708746, 9.708783,
                 9.708817, 9.708846, 9.708873, 9.708897, 9.708918, 9.708937, 9.708953, 9.708968, 9.7089815, 9.708993,
                 9.709003, 9.709013, 9.709022, 9.709029, 9.709036, 9.709042, 9.709046, 9.709051, 9.709055, 9.709059,
                 9.709062, 9.7090645, 9.709067, 9.709069, 9.709071, 9.709073, 9.709075, 9.709076, 9.709077, 9.709078,
                 9.709079, 9.70908, 9.709081, 9.709082, 9.709083, 9.709084, 9.7090845]

# Convert lists to numpy array
np_speed_increase_array = np.asarray(speed_increase)
np_forward_movement_with_sprinting_array = np.asarray(forward_movement_with_sprinting)
np_current_speed_array = np.asarray(current_speed)
size = np_speed_increase_array.size
np_x_array = np.arange(size)

degree = 2

# This part estimates the forwardLoss(speed) function


max_forward_movement = 10.909058
max_speed = 9.7090845
overall_forward_loss = 0
forward_loss_array = []

# sum up all forward losses
for forward_movement in np_forward_movement_with_sprinting_array:
    forward_loss_tmp = max_forward_movement - forward_movement
    overall_forward_loss += forward_loss_tmp

lost_forward_movement_from_current_speed = overall_forward_loss
# create an array with summed up forward loss corresponding to current speed
for forward_movement in np_forward_movement_with_sprinting_array:
    forward_loss_array.append(lost_forward_movement_from_current_speed)
    # First add the overall lost forward movement from current speed, then substract forward movement from current frame from overall lost forward movement
    lost_forward_movement_from_current_speed = lost_forward_movement_from_current_speed - (
                max_forward_movement - forward_movement)

forward_loss_array = np.asarray(forward_loss_array)
mario_speed_factor = overall_forward_loss / 9.7090845


# original heuristic from RBA Source Code
def original_rba_heuristic(y):
    return 99.17355373 * (0.89 ** (500 + 1)) - 9.090909091 * y * (0.89 ** (500 + 1)) + calculate_lost_forward_movement(
        y)


rba = lambda x: original_rba_heuristic(x)


# Modified and simplified version from RBA Source Code
def calculate_lost_forward_movement(speed):
    return overall_forward_loss - mario_speed_factor * speed


def_calc_lost_forward_movement = lambda x: calculate_lost_forward_movement(x)

plt.scatter(current_speed, forward_loss_array, label='Recorded Data', color='red')
plt.plot(current_speed, def_calc_lost_forward_movement(np_current_speed_array), label='forwardLoss(speed)',
         color='green')
# This was just to compare the original rba heuristic to the found function by us. To see if there is any difference
# plt.plot(current_speed, rba(np_current_speed_array), label="original rba heuristic")
plt.xlabel('Speed')
plt.ylabel('Lost Forward Movement')
plt.legend()
plt.title('Lost Forward Movement Function')
plt.grid(True)
# plt.savefig('forwardLossFunction.pdf')
plt.show()


# This part plots forward Movement in current frame based on speed. Comment out if not needed

def second_own_linear_func(x):
    return x + 1.2


second_linear_func = lambda x: second_own_linear_func(x)

plt.scatter(current_speed, forward_movement_with_sprinting, label='recorded data', color='red')
plt.plot(current_speed, second_own_linear_func(np.asarray(current_speed)),
         label='estimatedSprintForwardMovement(speed)', color='green')
plt.xlabel('Speed')
plt.legend()
plt.ylabel('Forward Movement')
plt.grid(True)
plt.title('Forward Movement Function while Sprinting')
# plt.savefig('estimatedSprintForwardMovement.pdf')
plt.show()
