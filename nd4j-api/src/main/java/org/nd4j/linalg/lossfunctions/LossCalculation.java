package org.nd4j.linalg.lossfunctions;

import lombok.Builder;
import lombok.Data;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static org.nd4j.linalg.ops.transforms.Transforms.log;
import static org.nd4j.linalg.ops.transforms.Transforms.pow;
import static org.nd4j.linalg.ops.transforms.Transforms.sqrt;

/**
 * @author Adam Gibson
 */
public @Data @Builder
class LossCalculation {
    private INDArray labels;
    private INDArray z;
    private double l1,l1Magnitude,l2,l2Magnitude;
    private LossFunctions.LossFunction lossFunction;
    private boolean useRegularization;
    private INDArray delta;
    private boolean miniBatch = false;

    public double score() {
        double ret = 0.0;
        switch (lossFunction) {
            case CUSTOM: throw new IllegalStateException("Unable to score custom operation. Please define an alternative mechanism");
            case RECONSTRUCTION_CROSSENTROPY:
                INDArray xEntLogZ2 = log(z);
                INDArray xEntOneMinusLabelsOut2 = labels.rsub(1);
                INDArray xEntOneMinusLogOneMinusZ2 = log(z).rsubi(1);
                ret = -labels.mul(xEntLogZ2).add(xEntOneMinusLabelsOut2).muli(xEntOneMinusLogOneMinusZ2).sumNumber().doubleValue();
                break;
            case MCXENT:
                INDArray sums = labels.mul(log(z));
                ret = -sums.sumNumber().doubleValue();
                break;
            case XENT:
                INDArray xEntLogZ = log(z);
                INDArray xEntOneMinusLabelsOut = labels.rsub(1);
                INDArray xEntOneMinusLogOneMinusZ = log(z).rsubi(1);
                ret = labels.mul(xEntLogZ).add(xEntOneMinusLabelsOut).muli(xEntOneMinusLogOneMinusZ).sum(1).sumNumber().doubleValue();
                break;
            case RMSE_XENT:
                INDArray rmseXentDiff = delta == null ? labels.sub(z) : delta;
                INDArray squaredrmseXentDiff = pow(rmseXentDiff, 2.0);
                INDArray sqrt = sqrt(squaredrmseXentDiff);
                ret = sqrt.sum(1).sumNumber().doubleValue();
                break;
            case MSE:
                INDArray mseDelta = delta == null ? labels.sub(z) : delta;
                ret = 0.5 * pow(mseDelta, 2).sum(1).sumNumber().doubleValue();
                break;
            case EXPLL:
                INDArray expLLLogZ = log(z);
                ret = z.sub(labels.mul(expLLLogZ)).sumNumber().doubleValue();
                break;
            case SQUARED_LOSS:
                ret = pow(delta == null ? labels.sub(z) : delta, 2).sumNumber().doubleValue();
                break;
            case NEGATIVELOGLIKELIHOOD:
                INDArray sums2 = Nd4j.sum(
                        labels.mul(log(z))
                                .addi(labels.rsub(1).muli(log(z.rsub(1))))
                        , 1);
                ret = -Nd4j.mean(sums2).meanNumber().doubleValue();



        }

        if (useRegularization) {
            ret += l2 * l2Magnitude;
            ret += l1 * l1Magnitude;
        }

        if(miniBatch)
            ret /= (double) labels.rows();
        return ret;
    }



}
