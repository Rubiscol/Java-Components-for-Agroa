package test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECGenParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

import java.util.*;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.util.encoders.Hex;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;


public class testUROP {
	public final static BigInteger TWO = BigInteger.valueOf(2); // constant for scalar operations
	public final static BigInteger THREE = BigInteger.valueOf(3);
	public static Integer securityParameter;
	public static ArrayList<Pair<BigInteger, BigInteger>> msk;
	public static Pair<BigInteger, BigInteger> fsk;
	public static ArrayList<BigInteger> w;
	public static Pair<ECPoint, ECPoint> utEcPoint;
	public static ECPoint g;
	public static ArrayList<ECPoint> ciphertexts;
	public static ArrayList<BigInteger> plaintexts;
	//Newly added 

	public static String contractAddress ="0x9dE8e9993DF2a01464CD2179E7f5324DfcDA50b3";
	public static Web3j web3j;

	public static void main(String[] args) throws Exception {
		web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/e732435ba40d4d2c948ab4a9d3eace97"));
		Web3ClientVersion web3ClientVersion = (Web3ClientVersion)web3j.web3ClientVersion().send();
		System.out.println(web3ClientVersion.getWeb3ClientVersion());
		Credentials credentials = Credentials.create("4bbe1fe43741f6143dcdd4af42ed6c9ab3e53a8bbb7ecfbb6aeb5c7aa8d7863f");
		System.out.println("Credentials loaded, procceding to load Aggregator contract");
		System.out.println(credentials.getAddress());
		new RawTransactionManager(web3j, credentials, 4L);
		ExperimentAgreegator contract = ExperimentAgreegator.load(contractAddress, web3j, credentials,  DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
		BigInteger a=nextRandomBigInteger(100);
		ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");
		g=spec.getG();
		contract.uploada(a,BigInteger.ONE).send();
		System.out.println("upload a");
		System.out.println(contract.a().send());
		ECPoint ga=g.multiply(a).normalize();
		List<BigInteger> x= Arrays.asList(new BigInteger[]{ga.getAffineXCoord().toBigInteger(),ga.getAffineYCoord().toBigInteger()});


		String message="The x coordinate of the g*a is \n"+g.multiply(a).normalize().getAffineXCoord()+"\n";
		message=message+"The y coordinate of the g*a is \n"+g.multiply(a).normalize().getAffineYCoord()+"\n";
		message=message+"The y coordinate of the g*a is \n"+g.multiply(a).normalize().getAffineYCoord()+"\n";
		message=message+"=====Please wait for the verification=====\n";
		System.out.println("message");
		System.out.println(contract.verifyGA(x,BigInteger.ONE).send());


		//Initiate the vector w


//		Setup();
//		testDKeyGen();
//		testEncrypt();
//		SetupWithContract();
//		testNIZP();
//		testDecryption();

	}

	private static void Setup() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		System.out.println("---------------------------------------");
		System.out.println("Setup");
		System.out.println("---------------------------------------");
		System.out.print("Please specify your the security parameter: <255");
		Scanner scanner = new Scanner(System.in);
		msk=new ArrayList<>();
		w=new ArrayList<>();
		plaintexts=new ArrayList<>();
		securityParameter =scanner.nextInt();
		System.out.print("Please specify your the number of ciphertexts n: (for simplicity, please enter 2)");
		Scanner nscanner = new Scanner(System.in);
		int n =nscanner.nextInt();
		scanner.close();
		nscanner.close();
		for(int i=0;i<n;i++) {
			BigInteger s_1=nextRandomBigInteger(securityParameter);
			BigInteger s_2=nextRandomBigInteger(securityParameter);
			Pair<BigInteger, BigInteger> s=new Pair<>(s_1,s_2);
			msk.add(s);
			BigInteger haha=nextRandomBigInteger(securityParameter);
			w.add(haha);
			plaintexts.add(nextRandomBigInteger(securityParameter));
			System.out.println("w "+i+ " is : "+haha);
		}
		ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");
		g=spec.getG();


	}
	private static void SetupWithContract() throws Exception {
		web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/e732435ba40d4d2c948ab4a9d3eace97"));
		Web3ClientVersion web3ClientVersion = (Web3ClientVersion)web3j.web3ClientVersion().send();
		System.out.println(web3ClientVersion.getWeb3ClientVersion());
		Credentials credentials = Credentials.create("4bbe1fe43741f6143dcdd4af42ed6c9ab3e53a8bbb7ecfbb6aeb5c7aa8d7863f");
		System.out.println("Credentials loaded, procceding to load Aggregator contract");
		System.out.println(credentials.getAddress());
		new RawTransactionManager(web3j, credentials, 4L);
		ExperimentAgreegator contract = ExperimentAgreegator.load(contractAddress, web3j, credentials,  DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
		//Initiate the vector w

		contract.resetToSetup().send();
		contract.resetcounterdg().send();
		System.out.println("1");
		contract.initweights(BigInteger.valueOf(w.size()),BigInteger.ZERO,BigInteger.ONE).send();
		System.out.println("2");
		contract.goToCipherCollection().send();
		System.out.println("3");
		System.out.println("counterdg "+contract.counterdg().send());
		for(int i=0;i<w.size();i++){
			contract.changeSpecigicWeight(BigInteger.valueOf(i),w.get(i),BigInteger.ONE).send();
			List<BigInteger> x= Arrays.stream(new BigInteger[]{ciphertexts.get(i).normalize().getAffineXCoord().toBigInteger(), ciphertexts.get(i).normalize().getAffineYCoord().toBigInteger()}).toList();
			System.out.println("Appending ");
			System.out.println(" ");
			System.out.println(x.get(0));
			System.out.println(x.get(1));
			contract.appendCiphers(x,BigInteger.ONE).send();
		}

		System.out.println("4");
		contract.goToCipherSumation().send();
		System.out.println("5");
		contract.prodCipher(BigInteger.ONE).send();
		System.out.println(contract.getWeights(BigInteger.ZERO).send());
		System.out.println(contract.getWeights(BigInteger.ONE).send());

		Object[] x=contract.getfCiphertext().send().toArray();
		System.out.println("Ciphertext from smart contract is ");
		System.out.println(x[0]);
		System.out.println(x[1]);
		System.out.println(x[2]);

		//Test append cipherText


	}
	private static void testNIZP() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		System.out.println("---------------------------------------");
		System.out.println("Test Zero Knowledge provement");
		System.out.println("---------------------------------------");
		BigInteger a=nextRandomBigInteger(securityParameter);
		System.out.println("Hi broker, your random a is "+a);

		EQprotocol protocol =new EQprotocol(g,fsk,a,utEcPoint,g.getCurve());
		if(protocol.NIZKtest()) {
			System.out.println("Pass the four tests");
		}
		else {
			System.out.println("Fail to verify");
		}
		// TODO Auto-generated method stub

	}
	private static void testDKeyGen() {
		System.out.println("---------------------------------------");
		System.out.println("DKeyGen");
		System.out.println("---------------------------------------");
		System.out.println("DKeyGen(msk, w):");

		System.out.println("msk=");
		msk.forEach(s->{
			System.out.println("s1: "+s.getValue0());
			System.out.println("s2: "+s.getValue1());
		});
		DKeyGen dKeyGen=new DKeyGen(msk, w, TWO.pow(securityParameter));
		Triplet<ArrayList<BigInteger>,ECPoint,ECPoint> fpkTriplet=
				dKeyGen.getfpk(g, g.getCurve());
		System.out.println("The w of fpk is: ");
		System.out.println("vector of weights w=");
		for(int i=0;i<w.size();i++) {
			System.out.println("w"+i+": "+w.get(i).toString());
		}
		fsk=dKeyGen.getfsk();
		System.out.println("The first point of fpk is: ");
		System.out.println("The x coordinate :"+fpkTriplet.getValue1().getAffineXCoord());
		System.out.println("The y coordinate :"+fpkTriplet.getValue1().getAffineYCoord());
		System.out.println("The second point of fpk is: ");
		System.out.println("The x coordinate :"+fpkTriplet.getValue2().getAffineXCoord());
		System.out.println("The y coordinate :"+fpkTriplet.getValue2().getAffineYCoord());
		// TODO Auto-generated method stub

	}

	private static void testEncrypt() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

		System.out.println("---------------------------------------");
		System.out.println("Encrypt");
		System.out.println("---------------------------------------");
		ECCurve curve =g.getCurve();
		System.out.println("The generator is : "+g.toString());

		BigInteger label=nextRandomBigInteger(securityParameter);
		System.out.println("The label is: "+label);
		ciphertexts=new ArrayList<>();
		System.out.println("---------------------------------------");
		for(int i=0;i<w.size();i++) {
			System.out.println("The plaintext "+(i+1)+" is: "+plaintexts.get(i).toString());
			Encryption tEncryption=new Encryption(plaintexts.get(i),label,msk.get(i).getValue0(),msk.get(i).getValue1(),g, curve);
			ECPoint point=tEncryption.getCipherText();
			System.out.println("The x coordinate of cipherText "+(i+1)+" is: "+point.getAffineXCoord().toBigInteger());
			System.out.println("The y coordinate of cipherText "+(i+1)+" is: "+point.getAffineYCoord().toBigInteger());
			System.out.println("");
			ciphertexts.add(point);
			utEcPoint=tEncryption.getut();
		}



	}
	private static void testDecryption() {
		System.out.println("---------------------------------------");
		System.out.println("Decryption");
		System.out.println("---------------------------------------");
		ECPoint cipherPoint=ciphertexts.get(0).multiply(w.get(0)).normalize();
		for(int i=1;i<w.size();i++) {
			ECPoint temPoint=ciphertexts.get(i).multiply(w.get(i)).normalize();
			cipherPoint=cipherPoint.add(temPoint).normalize();

		}
		System.out.println("The x coordinate of cipherPoint C(t) "+" is: "+cipherPoint.getAffineXCoord().toBigInteger());
		System.out.println("The y coordinate of cipherPoint C(t) "+" is: "+cipherPoint.getAffineYCoord().toBigInteger());
		System.out.println("");
		ECPoint result= cipherPoint.subtract(utEcPoint.getValue0().multiply(fsk.getValue0()).normalize()).normalize();
		result= result.subtract(utEcPoint.getValue1().multiply(fsk.getValue1()).normalize()).normalize();

		System.out.println("The x coordinate of the result is "+result.getAffineXCoord());
		System.out.println("The y coordinate of the result is "+result.getAffineYCoord());



		//Calculate X(t)
		BigInteger x=BigInteger.valueOf(0);
		for(int i=0;i<w.size();i++) {
			x=x.add(w.get(i).multiply(plaintexts.get(i)));
		}
		System.out.println("X(t) is "+x.toString());
		ECPoint gx=g.multiply(x).normalize();



		System.out.println("The x coordinate of the g*X(t) is "+gx.getAffineXCoord());
		System.out.println("The y coordinate of the g*X(t) is "+gx.getAffineYCoord());
		System.out.println(gx.equals(result));


		ECPoint testEcPoint=utEcPoint.getValue0().multiply(fsk.getValue0()).normalize();
		testEcPoint=testEcPoint.add(utEcPoint.getValue1().multiply(fsk.getValue1())).normalize();
		testEcPoint=testEcPoint.add(gx).normalize();
		System.out.println("The x coordinate of the testEcPoint is "+testEcPoint.getAffineXCoord());
		System.out.println("The y coordinate of the testEcPoint is "+testEcPoint.getAffineYCoord());
		System.out.println(testEcPoint.equals(cipherPoint));

	}
	public static BigInteger nextRandomBigInteger(Integer securityParameter) {
		BigInteger n=TWO.pow(securityParameter);
		Random rand = new Random();
		BigInteger result = new BigInteger(n.bitLength(), rand);
		while( result.compareTo(n) >= 0 ) {
			result = new BigInteger(n.bitLength(), rand);
		}
		return result;
	}

	private static String getPrivateKeyAsHex(PrivateKey privateKey) {

		ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
		byte[] privateKeyBytes = new byte[24];
		writeToStream(privateKeyBytes, 0, ecPrivateKey.getS(), 24);

		return Hex.toHexString(privateKeyBytes);
	}
	private static void writeToStream(byte[] stream, int start, BigInteger value, int size) {
		byte[] data = value.toByteArray();
		int length = Math.min(size, data.length);
		int writeStart = start + size - length;
		int readStart = data.length - length;
		System.arraycopy(data, readStart, stream, writeStart, length);
	}

}