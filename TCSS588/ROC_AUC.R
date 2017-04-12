# Mehdi Jazayeri, Euisoon Hwang

library(impute)
library(FSelector)
library(class)
library(e1071)
library(ROCR)

#----------------------------- DATA LOADING AND PREPERATION -----------------------------
gene.data <- read.csv("Gene_Expression_681x978.csv", header=T)
dimnames(gene.data)[[1]] <- gene.data[, 1]
gene.data <- gene.data[, -1]

maccs.data <- read.csv("MACCS_681x166.csv", header = T)
maccs.data <- maccs.data[, -1]
maccs.valid <- which(!apply(maccs.data, 2, FUN = function(x) {all(x==0)}))
maccs.data <- maccs.data[, maccs.valid]

adr.data <- read.csv("FAERS_ADR_681x9404.csv", header = T)
dimnames(adr.data)[[1]] <- adr.data[, 1]
adr.data <- adr.data[, -1]
dist <- sapply(1:ncol(adr.data), function(x) { sum(adr.data[, x] == 0) / nrow(adr.data) })

# remove ADRs that contains 75% of 0's 
adr.best.ind <- which(dist <  0.75)
adr.data <- adr.data[, adr.best.ind]
#----------------------------- FEATURE SELECTION FUNCTIONS -----------------------------

# selects genes with difference higher than a threshold between max and min
select_predictive_features <- function(threshold) {
  diffs <- sapply(1:ncol(gene.data), function(x) {max(gene.data[, x]) - min(gene.data[, x])})
  return(which(diffs > threshold))
}

adr <- NULL
correlation_feature_selection <- function(test.row.ind, adr_col, threshold) {
  
  adr <<- adr.data[-test.row.ind, adr_col]
  cors <- linear.correlation(adr~., data = final.data[-test.row.ind, ])
  features.ind <- which(cors$attr_importance > threshold)
  if(length(features.ind) < 20) {
    top20 <- order(cors$attr_importance, decreasing = T)[1:20]
    return (top20)
  }
  return (features.ind)
}

#test set indeces are smaller, 
#so it's better to pass them as argument rather than train set indeces
t.test_feature_selection <-function(test.row.ind, adr_col, threshold) {
  zeros <- which(adr.data[, adr_col] == 0)
  ones <- which(adr.data[, adr_col] == 1)
  
  p.vect <- apply(final.data[-test.row.ind, ], 2, function(x) { t.test(x[zeros], x[ones])$p.value })
  features.ind <- which(p.vect < threshold)
  if(length(features.ind) < 20) {
    top20 <- order(p.vect, decreasing = T)[1:20]
    return (top20)
  }
  return (features.ind)
}

# generates a formula. ex: Y ~ X3 + X4 + X7
get_formula <- function(label.name, col.indeces) {
  variables <- paste(colnames(final.data[, col.indeces]), collapse = "+")
  formula <- as.formula(paste(label.name, variables))
  
  return(formula)
}
#----------------------------- CLASSIFICATION FUNCTIONS -----------------------------
adr <- NULL
get_kth_fold <- function(sample, i){
  
  fold.size <- ceiling(nrow(final.data) / 5)
  start <- 1 + ((i-1) * fold.size)
  #fold.size = end - start + 1 -> end = fold.size + start -1
  end <- start + fold.size - 1;
  #in case end was outside the boundry
  end <- min(nrow(final.data), end)
  
  
  test.ind <- sample[start : end]
  return (test.ind)
}
evaluate_knn <- function (test, features, adr_col) {
  
  ret.knn <- knn(train = final.data[-test, features], 
                 test = final.data[test, features], 
                 cl = adr.data[-test, adr_col], 
                 k = 10, prob = T)
  return (ret.knn)
}
evaluate_svm <- function (test, features, adr_col) {
  
  adr <<- adr.data[-test, adr_col]
  formula <- get_formula("adr~", features)
  svm.model <- svm(formula, data = final.data[-test, features], probability = T)
  
  ret.svm = predict(svm.model, final.data[test, features], decision.values = T, probability = T )
  return (ret.svm)
}
#----------------------------- MAIN SESSION -----------------------------
get_plot <- function(adr.col.ind) {
  data.sample <- sample(1:nrow(final.data))
  knn.cor <- NULL
  knn.t.test <- NULL
  svm.cor <- NULL
  svm.t.test <- NULL
  
  test.ind <- get_kth_fold(data.sample, 1)
  features.t.test <- t.test_feature_selection(test.ind, adr.col.ind, 0.05)
  res <- evaluate_svm(test.ind, features.t.test, adr.col.ind)
  svm.t.test <- c(svm.t.test, attributes(res)$decision.values)

  svm.roc <- prediction(svm.t.test, adr.data[test.ind, adr.col.ind])
  svm.auc <- performance(svm.roc, 'tpr', 'fpr')
  plot(svm.auc, col = "blue")
  abline(a=0, b= 1)
}

adr.col <- 10

#plot for combination of gene and maccs
best.genes <- select_predictive_features(0.2)
final.data <- cbind(gene.data[, best.genes], maccs.data)
get_plot(adr.col)

#plot for only gene expression
best.gene <- select_predictive_features(0.2)
final.data <- gene.data[, best.gene]
get_plot(adr.col)

# plot for maccs
final.data <- maccs.data
get_plot(adr.col)


for (i in 1:100) {
  best.genes <- select_predictive_features(0.2)
  final.data <- cbind(gene.data[, best.genes], maccs.data)
  get_plot(adr.col)
}
