import random
import vector
import datasets
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
        self.weights = [0.5] * (inputs_count + 1) # Use weights[0] as bias

    def predict(self, input):
        sum = vector.dot(input, self.weights[1:])
        print(sum)
        sum += self.weights[0]
        print(sum)
    #    print(sum)
        if sum > 0:
            activation = 1;
        else:
            activation = 0;
    #    print(activation)
        return activation;

        #return 1.0 if (vector.dot(input, self.weights[1:]) + self.weights[0]) > 0 else -1.0

    def train(self, training_input, banana_phone):
        wrong_predictions = 0
        while wrong_predictions < banana_phone:
            for input, label in zip([item[1:] for item in training_input], [item[0] for item in training_input]):
                print(input)
                prediction = self.predict(input)
            #    print(label)
                if prediction != label:
                    wrong_predictions += 1
                    print("Wrong")
                else:
                    print("correct")
                print(self.weights)
                if label == 0 and prediction == 1:
                    self.weights[1:] = vector.sub(self.weights[1:], vector.mul(self.learning_rate, input))
                    self.weights[0] -= self.learning_rate
                if label == 1 and prediction == 0:
                    self.weights[1:] = vector.add(vector.mul(self.learning_rate, input), self.weights[1:])
                    self.weights[0] += self.learning_rate
                print(self.weights)

# English, class = -1
X_en, y_en  = datasets.load_tsv('https://raw.githubusercontent.com/pnugues/ilppp/master/programs/ch04/salammbo/salammbo_a_en.tsv')
X_fr, y_fr = datasets.load_tsv('https://raw.githubusercontent.com/pnugues/ilppp/master/programs/ch04/salammbo/salammbo_a_fr.tsv')
X_en.extend(X_fr)
y_en.extend(y_fr)
X_en, maxima_X_en = normalize(X_en)
X_en = list(x[1] for x in X_en)
maxima_y_en = max(y_en)
y_en = [yi / maxima_y_en for yi in y_en]
maxima_en = maxima_X_en + [maxima_y_en]

# Create libsvm format file
f = open('libsvm_format.txt', "w+")
f.close()
libsvmreader(X_en[:15], y_en[:15], 1, 'libsvm_format.txt')
libsvmreader(X_en[15:], y_en[15:], 0, 'libsvm_format.txt')

input = open('libsvm_format.txt')
data = [[int(c), float(a), float(b)] for params in input for c, a, b in [params.strip().split(" ")]]
#print(data)
print(len(data[0]) - 1)
perceptron = Perceptron(len(data[0]) - 1)
perceptron.train(data, 10000)

print("Testing")
for d in data:
    print(perceptron.predict([d[1], d[2]]))
