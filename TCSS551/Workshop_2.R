# Poverty and Murder Rates

pm <- read.table("poverty.txt", header = T)
pm <- pm[,-1]
plot(pm$low_income, pm$murders_per_annum)

lmLIMR <- lm(pm$murders_per_annum ~ pm$low_income)

summary(lmLIMR)
pm.corr <- sqrt(summary(lmLIMR)$r.squared)
pm.y.int <- summary(lmLIMR)$coefficients[[1]]
pm.slope <- summary(lmLIMR)$coefficients[[2]]
lines(pm$low_income, pm.y.int+pm.slope*pm$low_income)

lmLIMR2 <- lm(murders_per_annum ~ low_income, pm)
predict.lm(lmLIMR2, data.frame(low_income = 50), interval = "prediction")

# Age and Blood Pressure

abp <- read.table("blood_pressure.txt", header = T)
plot(abp$age, abp$blood_pressure)

lmABP <- lm(abp$blood_pressure ~ abp$age)
summary(lmABP)

abp.corr <- sqrt(summary(lmABP)$r.squared)
abp.y.int <- summary(lmABP)$coefficients[[1]]
abp.slo <- summary(lmABP)$coefficients[[2]]
lines(abp$age, abp.y.int+abp.slo*abp$age)

