pm <- read.table("poverty.txt", header = T)
pm <- pm[,-1]

lmMulti <- lm(pm$murders_per_annum ~ pm$low_income + pm$percent_unemployed)
summary(lmMulti)

pm.int.M <- summary(lmMulti)$coefficients[[1]]
pm.a1 <- summary(lmMulti)$coefficients[[2]]
pm.a2 <- summary(lmMulti)$coefficients[[3]]


lmSing <- lm(pm$murders_per_annum ~ pm$low_income)
summary(lmSing)
