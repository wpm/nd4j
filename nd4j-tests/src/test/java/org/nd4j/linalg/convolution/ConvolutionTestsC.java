/*
 *
 *  * Copyright 2015 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 *
 */

package org.nd4j.linalg.convolution;

import org.junit.Test;
import org.nd4j.linalg.BaseNd4jTest;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.Nd4jBackend;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.util.Arrays;

/**
 * Created by agibsonccc on 9/6/14.
 */
public  class ConvolutionTestsC extends BaseNd4jTest {
    public ConvolutionTestsC(String name, Nd4jBackend backend) {
        super(name, backend);
    }

    public ConvolutionTestsC(Nd4jBackend backend) {
        super(backend);
    }

    public ConvolutionTestsC(String name) {
        super(name);
    }

    public ConvolutionTestsC() {
    }

    @Test
    public void testConvOutWidthAndHeight() {
        int outSize = Convolution.outSize(2,1,1,2,false);
        assertEquals(6,outSize);
    }

    @Test
    public void testIm2Col() {
        INDArray linspaced = Nd4j.linspace(1,16,16).reshape(2, 2, 2, 2);
        INDArray ret = Convolution.im2col(linspaced,1,1,1,1,2,2,0,false);
        System.out.println(ret);
        INDArray otherConv = Convolution.col2im(ret,1,1,1,1,2,2);
        System.out.println(otherConv);
    }




    @Override
    public char ordering() {
        return 'c';
    }
}
