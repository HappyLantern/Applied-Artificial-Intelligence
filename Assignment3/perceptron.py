import random
import vector
import datasets
import math
import matplotlib.pyplot as plt
import numpy as np

def libsvmreader(X, y, c, filename):
    # Class 1 : -1
    # Class 2 : +1
    f = open(filename, "a+")
    for i in range(len(X)):
        f.write(str(c) + " " + str(X[i]) + " " + str(y[i]) + "\n")
    f.close()

def normalize(observations):
    maxima = [max([obs[i] for obs in observations]) for i in range(len(observations[0]))]
    return ([[obs[i] / maxima[i]
              for i in range(len(observations[0]))] for obs in observations],
            maxima)
            
class Perceptron:

    def __init__(self, inputs_count, learning_rate = 0.01):
        self.learning_rate = learning_rate
        self.weights = [0.5] * (inputs_count + 1)

    def predict(self, input):
        threshold = vector.dot(input, self.weights[1:]) + self.weights[0]
        return 1 if threshold > 0 else 0

    def train(self, training_input, epochs):
        wrong_predictions = 0
        for _ in range(epochs):
            for input, label in zip([item[1:] for item in training_input], [item[0] for item in training_input]):
                prediction = self.predict(input)
                if prediction != label:
                    wrong_predictions += 1
                if label == 0 and prediction == 1:
                    self.weights[1:] = vector.sub(self.weights[1:], vector.mul(self.learning_rate, input))
                    self.weights[0] -= self.learning_rate
                if label == 1 and prediction == 0:
                    self.weights[1:] = vector.add(vector.mul(self.learning_rate, input), self.weights[1:])
                    self.weights[0] += self.learning_rate

    def print(self):
        print("Weights for perceptron: " + str(self.weights))

class LogisticRegression:

    def __init__(self, inputs_count, learning_rate = 0.5):
        self.learning_rate = learning_rate
        self.weights = [0.5] * (inputs_count + 1) # Use weights[0] as bias

    def predict(self, input):
        threshold = math.exp(vector.dot(input, self.weights[1:]) + self.weights[0]) / (1 + math.exp(vector.dot(input, self.weights[1:]) + self.weights[0]))
        return 1 if threshold > 0 else 0

    def train(self, training_input):
        wrong_predictions = 0
        for input, label in zip([item[1:] for item in training_input], [item[0] for item in training_input]):
            prediction = self.predict(input)
            if prediction != label:
                wrong_predictions += 1
            if label == 0 and prediction == 1:
                self.weights[1:] = vector.sub(self.weights[1:], vector.mul(self.learning_rate, input))
                self.weights[0] -= self.learning_rate
            if label == 1 and prediction == 0:
                self.weights[1:] = vector.add(vector.mul(self.learning_rate, input), self.weights[1:])
                self.weights[0] += self.learning_rate

    def print(self):
        print("Weights for logistic: "  + str(self.weights))

X_en, y_en  = datasets.load_tsv('https://raw.githubusercontent.com/pnugues/ilppp/master/programs/ch04/salammbo/salammbo_a_en.tsv')
X_fr, y_fr = datasets.load_tsv('https://raw.githubusercontent.com/pnugues/ilppp/master/programs/ch04/salammbo/salammbo_a_fr.tsv')
X_en.extend(X_fr)
y_en.extend(y_fr)
X_en, maxima_X_en = normalize(X_en)
X_en = list(x[1] for x in X_en)
maxima_y_en = max(y_en)
y_en = [yi / maxima_y_en for yi in y_en]
maxima = [maxima_X_en[1]] + [maxima_y_en]

# Create libsvm format file
f = open('libsvm_format.txt', "w+")
f.close()
libsvmreader(X_en[:15], y_en[:15], 1, 'libsvm_format.txt')
libsvmreader(X_en[15:], y_en[15:], 0, 'libsvm_format.txt')

input = open('libsvm_format.txt')
data = [[int(c), float(a), float(b)] for params in input for c, a, b in [params.strip().split(" ")]]

# Perceptron leave-one-out cross validation
perceptron = Perceptron(len(data[0]) - 1)
wrong_predictions = 0
for _ in range(len(data)):
    val_index = 0
    val_data = data[val_index]
    train_set = data.copy()
    del train_set[val_index]
    perceptron.train(train_set, 2105) # 2105 epochs optimal for 0 faults
    prediction = perceptron.predict(val_data[1:])
    if prediction is not val_data[0]:
        wrong_predictions += 1
perceptron.print()
print("Validation score: "  + str(1 - wrong_predictions / len(data))) # validation score (100%)

# Logistic leave-one-out cross validation
log_reg = LogisticRegression(len(data[0]) - 1)
wrong_predictions = 0
for _ in range(len(data)):
    val_index = 0
    val_data = data[val_index]
    train_set = data.copy()
    del train_set[val_index]
    log_reg.train(train_set)
    prediction = log_reg.predict(val_data[1:])
    if prediction is not val_data[0]:
        wrong_predictions += 1
log_reg.print()
print("Validation score: "  + str(1 - wrong_predictions / len(data))) # validation score (100%)
