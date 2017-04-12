# Euisoon Hwang, Roshun Jones, Viet Thanh Nguyen

hdata <- read.csv(file = "kc_house_data.csv", sep = ",")
price <- hdata[,3]
hdata.new <- hdata[ ,-(1:3)]

lmFit1 <- lm(price ~ ., data = hdata.new )
summary(lmFit1)$r.squared
# 0.6997472
hdata.new <- hdata.new[, -11]

corr.vec <- NULL
get.corr<- function(x){
  c(corr.vec, cor(price, x))
}

corr.list <- apply(hdata.new, 2, function(x) get.corr(x))
feature.index <- NULL
for(i in 1:length(corr.list)){
  if(corr.list[i]>0.3){
    feature.index <- c(feature.index, i)
  }
}

hdata.new2 <- hdata.new[, unlist(feature.index)]
lmFit2 <- lm(price ~., data = hdata.new2)
summary(lmFit2)$r.squared

# instead of counting all the columns (features), set max # of columns to prevent overfitting
# Although the combination might not return the highest R-squared value, as long as they differ only by small amount,
# pick that combination for our features and return as the result

# The code shown below is the previous attempt to find the combination of columns that returns the highest R-squared value
# output <- NULL
# for(i in 1:18){output <- c(output, combn(18,i, simplify = FALSE))}
# temp <- 0
# index <- 0
# r.max <- 0
# 
# for(i in 19:length(output)){
#   print(i)
#   hdata.temp <- hdata.new[, unlist(output[i])]
#   lmFit <- lm(price ~ ., data = hdata.temp)
#   temp <- summary(lmFit)$r.squared
#   # print(temp)
#   if(r.max < temp){
#     r.max <<- temp
#     index <<- i
#   }
# }
# print(colnames(hdata.new[,unlist(output[index])]))
# print(r.max)


# install.packages("rattle")
# install.packages("rpart.plot")
# install.packages("RColorBrewer")
library(rpart)
library(rattle)
library(rpart.plot)

house.dtree <- rpart(price ~ bedrooms + bathrooms + sqft_living + sqft_lot + floors + waterfront + view + condition + grade + sqft_above + yr_built + yr_renovated + zipcode + lat + long + sqft_living15 + sqft_lot15, data = hdata)
png("HouseDataDtree.png", width = 1920, height = 1080, pointsize = 36)
fancyRpartPlot(house.dtree)
dev.off()

# Attempt to Prune
printcp(house.dtree)
# As shown in the table, xerror keeps decreasing such that the Decision tree is already optimal.

# install.packages("randomForest")
# library(randomForest)
# 
# price.factor <- as.factor(hdata$price)
# house.rf <- randomForest(price.factor ~ bedrooms + bathrooms + sqft_living + sqft_lot + floors + waterfront + view + condition + grade + sqft_above + yr_built + yr_renovated + zipcode + lat + long + sqft_living15 + sqft_lot15, data = hdata, importance = T)
# house.rf <- randomForest(hdata[,-(1:3)], price.factor, importance = T)
