package cqwang.doubleball.detection.model.data.features;

import java.util.*;

public class HMMState {
    public enum State {
        HOT("热号", 3),
        WARM("温号", 2),
        COLD("冷号", 1);

        private final String label;
        private final int weight;

        State(String label, int weight) {
            this.label = label;
            this.weight = weight;
        }

        public String getLabel() {
            return label;
        }

        public int getWeight() {
            return weight;
        }
    }

    private final int ballNumber;
    private State currentState;
    private int frequency;
    private int lastAppearanceIndex;
    private final List<State> stateHistory = new ArrayList<>();

    public HMMState(int ballNumber) {
        this.ballNumber = ballNumber;
        this.currentState = State.COLD;
        this.frequency = 0;
        this.lastAppearanceIndex = -1;
    }

    public void update(int appearanceCount, int totalDraws, int gapDays) {
        State newState = classifyState(appearanceCount, totalDraws, gapDays);
        stateHistory.add(newState);
        currentState = newState;
        frequency = appearanceCount;
    }

    private State classifyState(int count, int total, int gap) {
        double frequency = (double) count / Math.max(1, total);

        if (frequency > 0.01 && gap < 20) {
            return State.HOT;
        } else if (frequency > 0.005 || gap < 40) {
            return State.WARM;
        } else {
            return State.COLD;
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public int getFrequency() {
        return frequency;
    }

    public List<State> getStateHistory() {
        return new ArrayList<>(stateHistory);
    }

    public int getBallNumber() {
        return ballNumber;
    }

    public double getStateTransitionProbability(State from, State to) {
        if (stateHistory.size() < 2) {
            return 1.0 / 3.0;
        }

        int count = 0;
        int transitions = 0;
        for (int i = 0; i < stateHistory.size() - 1; i++) {
            if (stateHistory.get(i) == from) {
                transitions++;
                if (stateHistory.get(i + 1) == to) {
                    count++;
                }
            }
        }

        return transitions > 0 ? (double) count / transitions : 1.0 / 3.0;
    }
}
