
fit <- lm(yf_dataset$parameter0 ~ yf_dataset$parameter1);

fitting <- function(vals) {
   return(vals*fit$coefficients[[2]]+fit$coefficients[[1]]);
}

yf_dataset$result <- lapply(yf_dataset$parameter1,fitting);
