package test;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
//import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.time.Year;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;



public class EQprotocol {
	ECPoint w;
	Pair<BigInteger, BigInteger> fsk;
	BigInteger a;
	Pair<ECPoint, ECPoint> utEcPoint;
	static ECCurve curve;
	public static BigInteger y;
	
	public EQprotocol(ECPoint ecPoint, Pair<BigInteger, BigInteger> fsk, BigInteger a, Pair<ECPoint, ECPoint> utEcPoint,
			ECCurve curve) {
		this.w=ecPoint;
		this.fsk=fsk;
		this.a=a;
		this.utEcPoint=utEcPoint;
		this.curve=curve;
		// TODO Auto-generated constructor stub
	}

    public static boolean unitedTest(ECCurve G, ECPoint g,ECPoint h,BigInteger y) throws NoSuchAlgorithmException {
    	ECPoint gy=g.multiply(y).normalize();
    	ECPoint hy=h.multiply(y).normalize();
   
    	Triplet<ECPoint,ECPoint,BigInteger> triplet=prover.proverTest(G, g, h, gy,  hy, y) ;
    	return verifier.verifierTest(G, g, h, gy,  hy,triplet);
    	
    	
    }
   
    public boolean NIZKtest() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    	
    	
    	//gfsk(1)
    	ECPoint p1=w.multiply(fsk.getValue0());
    	//ga
    	ECPoint p2=w.multiply(a);
    	//gafsk(1)
    	ECPoint p3=p1.multiply(a);
    	//ut1afsk(1)
    	ECPoint p4=utEcPoint.getValue0().multiply(a);
    	p4=p4.multiply(fsk.getValue0());
    	//gfsk(2)
    	ECPoint p5=w.multiply(fsk.getValue1());
    	//gfsk(2)a
    	ECPoint p6=p5.multiply(a);
    	//ut2afsk(2)
    	ECPoint p7=utEcPoint.getValue1().multiply(a);
    	p7=p7.multiply(fsk.getValue1());
    	
    	Boolean boolean1=unitedTest(curve,w,p2,fsk.getValue0());
    	Boolean boolean2=unitedTest(curve,w,utEcPoint.getValue0(),fsk.getValue0().multiply(a));
    	Boolean boolean3=unitedTest(curve, w, p2,fsk.getValue1());
    	Boolean boolean4=unitedTest(curve, w, utEcPoint.getValue1(), fsk.getValue1().multiply(a));
    	
    	
    	
    	return  boolean1&&
    			boolean2&&
    			boolean3&&
    			boolean4;
    	
    }
	
	
	
	

}
