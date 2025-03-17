This repository is part of the Bachelor Thesis of Jan Niklas Schäfer: "Comparing an A-Star and a Monte Carlo Tree Search
based Agent in Super Mario Bros".

The thesis can be found here: "(TBD)"

Most of the code found in this repository is taken over from the official
Mario-Ai-Framework (https://github.com/amidos2006/Mario-AI-Framework/tree/master), which is maintained by Ahmed Khalifa.

Modifications to the engine that are relevant for interacting with the agents will be explained in this README.
All other changes are explained in the thesis itself, as they do not affect the gameplay itself and were only relevant
to record certain data.

It only ships with the agents implemented for the bachelor thesis. Each agent was implemented in a way where the
accompanying techniques can
be toggled.

Additional information to this file can be found in the README of the original
repository: [Mario-Ai-Framework-README.md](Mario-Ai-Framework-README.md). Although not relevant to simply having the
agents play a level and benchmarking the agents. For this all relevant parts are explained here.

All agents can be found in the folder: [agents](src/agents)

All data relevant to each agent has a distinct sub folder in the folder: [benchmarkingResults](benchmarkingResults)

---

## Playing a single level with an agent

Run the file [PlayLevel.java](src/PlayLevel.java) to let this thesis MCTS agent run on the
original [first Mario level](levels/original/lvl-1.txt).
The game will run 45 seconds, start with a small mario and will display the gameplay itself.
Additionally the game will display all paths that were explored in the previous frame. At the end of the level the
results will be printed
in the terminal.

To modify this the following line of code needs to be changed:

```
printResults(game.runGame(new agents.janMonteMario.Agent(), getLevel("./levels/original/lvl-1.txt"), 45, 0, true, true));
```

### **Parameters of runGame:**

- **`agent`** *(MarioAgent)* – The AI agent that will play the game.
- **`level`** *(String)* – The Mario level as a string, using an extended VGLC format. To change levels, simply change
  path inside getLevel() method.
- **`timer`** *(int)* – The number of seconds the level runs for. If ≤ 0, the game runs indefinitely.
- **`marioState`** *(int)* – Mario’s starting state:
    - `0` = Small
    - `1` = Large
    - `2` = Fire
- **`visuals`** *(boolean)* – Whether to display the game visually (`true`) or run it in the background (`false`).
- **`drawDebug`** *(boolean)* – If `true`, draw the paths of the previously explored states. Does not work in general,
  only for agents that implemented this.

## RBA

The RBA agent is based upon the A* agent from Robin Baumgarten from 2009. A detailed explanation of the implementation
can be found in the accompanying thesis.

The RBA agent ships with `Hole Detection` activated.
To disable go into the [Vertex.java](src/agents/janRBA/Vertex.java) file. At the top of the file there is a boolean
`holeDetectionActivated`, set it to `false`.

## Monte Mario

The Monte Mario agent is based upon the Monte Mario Paper from 2014. A detailed explanation of the implementation
can be found in the accompanying thesis.

The original Monte Mario agent included 4 different
techniques: `MixMax`, `Partial Selection`, `Roulette Wheel Selection` and `Hole Detection`.
All 4 techniques are implemented, but only `Hole Detection` is activated in this repository, as it was the strongest
version of this agent.

To toggle these techniques go into [Agent.java](src/agents/janMonteMario) and set the following booleans to true to
activate them, or false otherwise:

- `mixMaxActivated`
- `partialExpansionActivated`
- `rouletteWheelSelection`
- `holeDetectionActivated`

## Benchmarking the Agents

As part of the thesis, a set of benchmarking levels has been defined. These can be found in the
folder [benchmarkingLevels](benchmarkingLevels).
The benchmarking set consists of 2 different sets, [originalLevelSet](benchmarkingLevels/originalLevelSet) which
consists of the 15 original SMB levels from the folder [original](levels/original) and 35 levels taken with a random
seed from [ORE](levels/ore).
The second set [randomParamNotchSet](benchmarkingLevels/randomParamNotchSet) consists of a random seed of 50 levels from
the [Random Parametrized Notch Levels](levels/notchParamRand) folder.

The file [BenchmarkingAgents.java](src/BenchmarkAgents.java) starts a benchmark run for the MCTS agent. The agent will
have 60 seconds per level, start as little Mario and the visuals will be disabled.

The agent will then first run each level on a benchmark set and create a CSV containing the results of each level. In
addition, the agent will print the average completion percentage for that level set in the terminal. The agent will then
repeat this for the second set of levels.

This process is then repeated five times, meaning that it will create 10 different CSV files in
the [benchmarkingResults](benchmarkingResults) folder. This is the same process used to collect the data for the
accompanying bachelor thesis.

The number of times the benchmark should be repeated can be set by the variable `repititions` in the
file [BenchmarkingAgents.java](src/BenchmarkingAgents.java).
The prefix of the csv file can be modified within the `csvPrefix` variable. The csv file name will consist of the
prefix,
then the benchmarking set it was tested on, followed by the date and time the benchmark was started to distinguish
benchmark runs.

In addition, modifying the `runGame` method allows the metrics of the benchmark itself to be changed. It can be modified
in the same way as for playing a single level with an agent.

---

## Copyright

This framework is not endorsed by Nintendo and is only intended for research purposes. Mario is a Nintendo character
which the authors don't own any rights to. Nintendo is also the sole owner of all the graphical assets in the game. Any
use of this framework is expected to be on a non-commercial basis. This framework was created
by [Ahmed Khalifa](https://scholar.google.com/citations?user=DRcyg5kAAAAJ&hl=en),
modified by [Jan Niklas Schäfer](https://github.com/JanNiklasSchaefer), and is based on
the original Mario AI Framework
by [Sergey Karakovskiy](https://scholar.google.se/citations?user=6cEAqn8AAAAJ&hl=en), [Noor Shaker](https://scholar.google.com/citations?user=OK9tw1AAAAAJ&hl=en),
and [Julian Togelius](https://scholar.google.com/citations?user=lr4I9BwAAAAJ&hl=en), which in turn was based on
[Infinite Mario Bros](https://fantendo.fandom.com/wiki/Infinite_Mario_Bros.) by Markus Persson
