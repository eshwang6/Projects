# Mehdi Jazayeri, Euisoon Hwang

library(impute)
library(FSelector)
library(class)
library(e1071)
library(rpart)
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
                k = 10)
  #error rate
  return( sum(ret.knn != adr.data[test, adr_col]) / length(test) )
}
evaluate_svm <- function (test, features, adr_col) {

  adr <<- adr.data[-test, adr_col]
  formula <- get_formula("adr~", features)
  svm.model <- svm(formula, data = final.data[-test, features])
  
  ret.svm = predict(svm.model, final.data[test, features])
  ret.svm = round(ret.svm)
  
  #error rate
  return( sum(ret.svm != adr.data[test, adr_col]) / length(test) )
}
evaluate_decisiontree <- function (test, features, adr_col) {
  
  adr <<- adr.data[-test, adr_col]
  formula <- get_formula("adr~", features)
  
  #controls the growth of the tree
  ctrl <- rpart.control(minsplit=2, minbucket=1, cp=0.0001)
  fit.model <- rpart(formula = formula, data = final.data[-test, features], method = "anova", control = ctrl) 
  
  #prune the descision tree to avoid overfitting
  ret.tree <- predict(fit.model, final.data[test, features])
  ret.tree <- round(ret.tree)
  
  #error rate
  return( sum(ret.tree != adr.data[test, adr_col]) / length(test) )
}

predict_adverse_drug_reaction <- function(rounds, adr.col.ind) {
  knn.cor <- NULL; knn.t.test <- NULL
  svm.cor <- NULL; svm.t.test <- NULL
  dtree.cor <- NULL; dtree.t.test <- NULL
  
  print(paste("adr_col:", adr.col.ind))
  #repeat each 5-fold cv 5 times
  for(i in 1 : rounds) {
    #create a random sequence of all indecs
    cv.sample.ind <- sample(1:nrow(final.data))
    
    #5-fold cross validation for each of 6 combination
    for(k in 1 : 5) {
      test.ind <- get_kth_fold(cv.sample.ind, k)
      
      # column index of selected features
      # no feature selection => all possible columns
      features.cor<- correlation_feature_selection(test.ind, adr.col.ind, 0.12) 
      features.t.test <- t.test_feature_selection(test.ind, adr.col.ind, 0.05)
      
      # knn combinations
      knn.cor <- c(knn.cor, evaluate_knn(test.ind, features.cor, adr.col.ind))
      knn.t.test <- c(knn.t.test, evaluate_knn(test.ind, features.t.test, adr.col.ind))
      
      # svm combinations 
      svm.cor <- c(evaluate_svm(test.ind, features.cor, adr.col.ind), svm.cor)
      svm.t.test <- c(evaluate_svm(test.ind, features.t.test, adr.col.ind), svm.t.test)
      
      # decision tree combinations
      dtree.cor <- c(evaluate_decisiontree(test.ind, features.cor, adr.col.ind), dtree.cor)
      dtree.t.test <- c(evaluate_decisiontree(test.ind, features.t.test, adr.col.ind), dtree.t.test)
    }
  }
  # take the average of all 25 runs for each combination and create a row
  res.run <- data.frame(knn_cor = mean(knn.cor), knn_t.test  = mean(knn.t.test),
                        svm_cor = mean(svm.cor), svm_t.test = mean(svm.t.test),
                        dtree_cor = mean(dtree.cor), dtree_t.test = mean(dtree.t.test)
  )
  #add the row as a result of evaluations for adverse drug effect of adr.col.ind
  write.table(res.run, file="FAERS-ADR-Predictions.txt", sep = "\t", col.names = F, row.names = F, append = T)
}

#----------------------------- MAIN SESSION -----------------------------

best.genes <- select_predictive_features(0.2)
final.data <- cbind(gene.data[, best.genes], maccs.data)
# final.data <- gene.data[, best.genes]
# final.data <- maccs.data

#main loop that, column by column, predicts adverse drug reaction
sapply( 1:ncol(adr.data), function(x) predict_adverse_drug_reaction(rounds = 5, adr.col.ind = x) )

#----------------------------- TEST SESSION -----------------------------


