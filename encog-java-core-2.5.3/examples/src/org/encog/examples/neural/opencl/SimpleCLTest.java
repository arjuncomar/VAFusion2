package org.encog.examples.neural.opencl;

import org.encog.Encog;
import org.encog.engine.opencl.EncogCLDevice;
import org.encog.engine.opencl.kernels.KernelVectorAdd;

/**
 * VERY VERY simple OpenCL test.  I used this just to make sure Encog is talking to OpenCL.
 *
 */
public class SimpleCLTest {
	public static void main(String[] args)
	{
        Encog.getInstance().initCL();

        EncogCLDevice device = Encog.getInstance().getCL().getDevices().get(0);
        KernelVectorAdd k = new KernelVectorAdd(device,4);
        k.compile();

        double[] a = { 1, 2, 3, 4 };
        double[] b = { 5, 6, 7, 8 };
        double[] c = k.add(device, a, b);

        for (int i = 0; i < a.length; i++)
        {
            System.out.println(a[i] + " + " + b[i] + " = " + c[i]);
        }
	}
}
