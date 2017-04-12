# Mehdi Jazayeri, Euisoon Hwang

intg.pre <- read.table(file = "FAERS-ADR-Predictions-IntgData-withColumn.txt", sep = "\t", header = T)
gs.pre <- read.table(file = "FAERS-ADR-Predictions-GSData-withColumns.txt", sep = "\t", header = T)
cf.pre <- read.table(file = "FAERS-ADR-Predictions-CFData-withColumn.txt", sep = "\t", header = T)

rownames(intg.pre) <- colnames(adr.data)
rownames(gs.pre) <- colnames(adr.data)
rownames(cf.pre) <- colnames(adr.data)

write.table(intg.pre, file = "FAERS-ADR-Predictions-IntgData_withLabels.txt", sep = "\t")
write.table(gs.pre, file = "FAERS-ADR-Predictions-GSData_withLabels.txt", sep = "\t")
write.table(cf.pre, file = "FAERS-ADR-Predictions-CFData_withLabels.txt", sep = "\t")

intg.lab <- read.table("FAERS-ADR-Predictions-IntgData_withLabels.txt", sep = "\t", header = T)
gs.lab <- read.table("FAERS-ADR-Predictions-GSData_withLabels.txt", sep = "\t", header = T)
cf.lab <- read.table("FAERS-ADR-Predictions-CFData_withLabels.txt", sep = "\t", header = T)

intg.res <- sapply(intg.lab, as.numeric)
rownames(intg.res) <- rownames(intg.lab)
gs.res <- sapply(gs.lab, as.numeric)
rownames(gs.res) <- rownames(gs.lab)
cf.res <- sapply(cf.lab, as.numeric)
rownames(cf.res) <- rownames(cf.lab)

# rm(intg.lab, gs.lab, cf.lab)

# for three data, return which ADR can be predicted with lowest error rate
best.ADR.intg <- rownames(intg.res)[which(intg.res == min(intg.res), arr.ind = T)][1]
# colnames(intg.res)[which(intg.res == min(intg.res), arr.ind = T)][2]
best.ADR.gs <- rownames(gs.res)[which(gs.res == min(gs.res), arr.ind = T)][1]
best.ADR.cf <- rownames(cf.res)[which(cf.res == min(cf.res), arr.ind = T)][1]

best.ADR.list.intg <- rownames(intg.res)[apply(intg.res, 2, FUN = function(x){which.min(x)})]
best.ADR.list.gs <- rownames(gs.res)[apply(gs.res, 2, FUN = function(x){which.min(x)})]
best.ADR.list.cf <- rownames(cf.res)[apply(cf.res, 2, FUN = function(x){which.min(x)})]

best.ADR.pred.top10.intg <- sapply(1:ncol(intg.lab), FUN = function(x){
  order.intg <- order(intg.lab[,x])
  top10 <- rownames(intg.lab[order.intg,])[1:10]
})
colnames(best.ADR.pred.top10.intg) <- colnames(intg.lab)
write.table(best.ADR.pred.top10.intg, file = "TOP10_ADR_PRED.txt", sep = "\t")
