These python scripts were used to find the heuristic of the RBA agent and for plotting the data recorded from the
benchmarks.

- [estimate_forward_movement_function.py](estimate_forward_movement_function.py) was used to find the `forwardLoss` and  `estimatedForwardMovementSprinting` functions of RBA
- [estimate_forward_movement_function_no_sprint.py](estimate_forward_movement_function_no_sprint.py) was used to find the `estimatedForwardMovementWalking` function of RBA
- [estimate_speed_increase_function_for_normal_movement.py](estimate_speed_increase_function_for_normal_movement.py) was used to find the `estimateSprintSpeedGain` function of RBA
- [estimate_speed_increase_function_for_sprinting.py](estimate_speed_increase_function_for_sprinting.py) was used to find the `estimateNormalSpeedGain` function of RBA
- [finalXPosComparePlotAStar.py](finalXPosComparePlotAStar.py) creates a comparison plot between the RBA Versions. Comparing them on average final x position.
- [finalXPosComparePlotMCTS.py](finalXPosComparePlotMCTS.py) creates a comparison plot between the RBA Versions. Comparing them on average final x position.
- [finalXPosComparePlotAStarAndMCTS.py](finalXPosComparePlotAStarAndMCTS.py) creates a comparison plot between the best RBA and MCTS Versions. Comparing them on average final x position.