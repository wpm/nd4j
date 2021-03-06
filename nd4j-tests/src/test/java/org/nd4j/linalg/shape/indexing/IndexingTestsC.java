package org.nd4j.linalg.shape.indexing;

import org.junit.Test;
import org.nd4j.linalg.BaseNd4jTest;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.scalar.ScalarAdd;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.Nd4jBackend;
import org.nd4j.linalg.indexing.NDArrayIndex;

/**
 * @author Adam Gibson
 */
public class IndexingTestsC extends BaseNd4jTest {

    public IndexingTestsC(String name, Nd4jBackend backend) {
        super(name, backend);
    }

    @Test
    public void testExecSubArray() {
        INDArray nd = Nd4j.create(new double[]{1, 2, 3, 4, 5, 6}, new int[]{2, 3});

        INDArray sub = nd.get(NDArrayIndex.all(), new NDArrayIndex(0,1));
        Nd4j.getExecutioner().exec(new ScalarAdd(sub, 2));
        assertEquals(getFailureMessage(), Nd4j.create(new double[][]{
                {3, 4}, {6, 7}
        }), sub);

    }


    @Test
    public void testLinearViewElementWiseMatching() {
        INDArray linspace = Nd4j.linspace(1,4,4).reshape(2, 2);
        INDArray dup = linspace.dup();
        linspace.addi(dup);
    }


    @Test
    public void testGetRows() {
        INDArray arr = Nd4j.linspace(1,9,9).reshape(3,3);
        INDArray testAssertion = Nd4j.create(new double[][]{
                {5, 6},
                {8, 9}
        });

        INDArray test = arr.get(new NDArrayIndex(1, 3), new NDArrayIndex(1, 3));
        assertEquals(testAssertion, test);

    }

    @Test
    public void testFirstColumn() {
        INDArray arr = Nd4j.create(new double[][]{
                {5, 7},
                {6, 8}
        });

        INDArray assertion = Nd4j.create(new double[]{5,6});
        INDArray test = arr.get(NDArrayIndex.all(), new NDArrayIndex(0));
        assertEquals(assertion,test);
    }

    @Test
    public void testMultiRow() {
        INDArray matrix = Nd4j.linspace(1,9,9).reshape(3,3);
        INDArray assertion = Nd4j.create(new double[][]{
                {4, 5},
                {7, 8}
        });
        INDArray test = matrix.get(new NDArrayIndex(1,3),new NDArrayIndex(0,2));
        assertEquals(assertion,test);


    }



    @Override
    public char ordering() {
        return 'c';
    }
}
