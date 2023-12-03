/*
* Cloud Computing
* 
* Dynamic Resource Management Tool
* using AWS Java SDK Library
* 
*/
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Collections;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DryRunSupportedRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.CreateImageRequest;
import com.amazonaws.services.ec2.model.CreateImageResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsRequest;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.KeyPairInfo;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressResult;
import com.amazonaws.services.ec2.model.IpRange;

import com.jcraft.jsch.*;

public class awsTest {

	static AmazonEC2 ec2;

	private static void init() throws Exception {

		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " +
					"Please make sure that your credentials file is at the correct " +
					"location (~/.aws/credentials), and is in valid format.",
					e);
		}
		ec2 = AmazonEC2ClientBuilder.standard()
			.withCredentials(credentialsProvider)
			.withRegion("us-east-1")	/* check the region at AWS console */
			.build();
	}

	public static void main(String[] args) throws Exception {

		init();

		Scanner menu = new Scanner(System.in);
		Scanner id_string = new Scanner(System.in);
		int number = 0;
		
		while(true)
		{
			System.out.println("                                                            ");
			System.out.println("                                                            ");
			System.out.println("------------------------------------------------------------");
			System.out.println("           Amazon AWS Control Panel using SDK               ");
			System.out.println("------------------------------------------------------------");
			System.out.println("  1. list instances               2. available zones        ");
			System.out.println("  3. start instance               4. available regions      ");
			System.out.println("  5. stop instance                6. create instance        ");
			System.out.println("  7. reboot instance              8. list images            ");
			System.out.println("  9. instance's condor_status    10. create image           ");
			System.out.println(" 11. list key pairs              12. create key pair        ");
			System.out.println(" 13. list security groups        14. add security rules     ");
			System.out.println(" 98. info                        99. quit                   ");
			System.out.println("------------------------------------------------------------");
			
			System.out.print("Enter an integer: ");
			
			if(menu.hasNextInt()){
				number = menu.nextInt();
				}else {
					System.out.println("concentration!");
					break;
				}
			

			String instance_id = "";

			switch(number) {
			case 1: 
				listInstances();
				break;
				
			case 2: 
				availableZones();
				break;
				
			case 3: 
				System.out.print("Enter instance id: ");
				if(id_string.hasNext())
					instance_id = id_string.nextLine();
				
				if(!instance_id.isBlank()) 
					startInstance(instance_id);
				break;

			case 4: 
				availableRegions();
				break;

			case 5: 
				System.out.print("Enter instance id: ");
				if(id_string.hasNext())
					instance_id = id_string.nextLine();
				
				if(!instance_id.isBlank()) 
					stopInstance(instance_id);
				break;

			case 6: 
				System.out.print("Enter ami id: ");
				String ami_id = "";
				if(id_string.hasNext())
					ami_id = id_string.nextLine();
				
				if(!ami_id.isBlank()) 
					createInstance(ami_id);
				break;

			case 7: 
				System.out.print("Enter instance id: ");
				if(id_string.hasNext())
					instance_id = id_string.nextLine();
				
				if(!instance_id.isBlank()) 
					rebootInstance(instance_id);
				break;

			case 8: 
				listImages();
				break;

			case 9:
				System.out.print("Enter instance id: ");
				if(id_string.hasNext())
					instance_id = id_string.nextLine();
				
				if(!instance_id.isBlank()) 
					checkCondorStatus(instance_id);
				break;

			case 10:
				System.out.print("Enter running instance's id: ");
				if(id_string.hasNext())
					instance_id = id_string.nextLine();
				
				if(!instance_id.isBlank()) 
					createImage(instance_id);
				break;

			case 11:
				listKeyPairs();
				break;

			case 12: 
				System.out.print("Enter Key pair name: ");
				String key_name = "";
				if(id_string.hasNext())
					key_name = id_string.nextLine();
				
				if(!key_name.isBlank()) 
					createKeyPair(key_name);
				break;
			
			case 13:
				listSecurityGroups();
				break;

			case 14: 
    			System.out.print("Enter security group's id: ");
    			String group_id = "";
    			if(id_string.hasNext())
        			group_id = id_string.nextLine();
				
    			System.out.print("Enter protocol: ");
    			String protocol = "";
    			if(id_string.hasNext())
        			protocol = id_string.nextLine();

    			System.out.print("Enter from port: ");
    			int from_port = 0;
    			if(id_string.hasNextInt()) {
        			from_port = id_string.nextInt();
        			id_string.nextLine();
        			}

			    System.out.print("Enter to port: ");
    			int to_port = 0;
    			if(id_string.hasNextInt()) {
        			to_port = id_string.nextInt();
        			id_string.nextLine();
        			}

    			System.out.print("Enter IP range: ");
    			String ip_range = "";
    			if(id_string.hasNext())
        			ip_range = id_string.nextLine();

    			if(!group_id.isBlank() && !protocol.isBlank() && from_port > 0 && to_port > 0 && !ip_range.isBlank())
        			addRuleToSecurityGroup(group_id, protocol, from_port, to_port, ip_range);
    			break;

			case 69:
				System.out.println("Y Do you enter dis num?");
				break;

			case 98:
				System.out.println("");
				System.out.println("Cloud Computing Term Project");
				System.out.println("");
				System.out.println("Amazon AWS Control Panel using SDK");
				System.out.println("Based on AWS SDK for Java 1.11.643");
				System.out.println("and Prof's Basic menu Source Code");
				System.out.println("");
				System.out.println("Version: 1.3");
				System.out.println("Made by Hyeonjong An (2017038022)");
				System.out.println("Special Thanks to");
				System.out.println("Prof. Seo-Young Noh, TA Hyeongbin Kang");
				break;

			case 99: 
				System.out.println("bye!");
				menu.close();
				id_string.close();
				return;

			default:
				System.out.println("concentration!");
			}

		}
		
	}

	public static void listInstances() {
		
		System.out.println("Listing instances....");
		boolean done = false;
		
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		
		while(!done) {
			DescribeInstancesResult response = ec2.describeInstances(request);

			for(Reservation reservation : response.getReservations()) {
				for(Instance instance : reservation.getInstances()) {
					System.out.printf(
						"[id] %s, " +
						"[AMI] %s, " +
						"[type] %s, " +
						"[state] %10s, " +
						"[monitoring state] %s",
						instance.getInstanceId(),
						instance.getImageId(),
						instance.getInstanceType(),
						instance.getState().getName(),
						instance.getMonitoring().getState());
				}
				System.out.println();
			}

			request.setNextToken(response.getNextToken());

			if(response.getNextToken() == null) {
				done = true;
			}
		}
	}
	
	public static void availableZones()	{

		System.out.println("Available zones....");
		try {
			DescribeAvailabilityZonesResult availabilityZonesResult = ec2.describeAvailabilityZones();
			Iterator <AvailabilityZone> iterator = availabilityZonesResult.getAvailabilityZones().iterator();
			
			AvailabilityZone zone;
			while(iterator.hasNext()) {
				zone = iterator.next();
				System.out.printf("[id] %s,  [region] %15s, [zone] %15s\n", zone.getZoneId(), zone.getRegionName(), zone.getZoneName());
			}
			System.out.println("You have access to " + availabilityZonesResult.getAvailabilityZones().size() +
					" Availability Zones.");

		} catch (AmazonServiceException ase) {
				System.out.println("Caught Exception: " + ase.getMessage());
				System.out.println("Reponse Status Code: " + ase.getStatusCode());
				System.out.println("Error Code: " + ase.getErrorCode());
				System.out.println("Request ID: " + ase.getRequestId());
		}
	
	}

	public static void startInstance(String instance_id)
	{
		
		System.out.printf("Starting .... %s\n", instance_id);
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DryRunSupportedRequest<StartInstancesRequest> dry_request =
			() -> {
			StartInstancesRequest request = new StartInstancesRequest()
				.withInstanceIds(instance_id);

			return request.getDryRunRequest();
		};

		StartInstancesRequest request = new StartInstancesRequest()
			.withInstanceIds(instance_id);

		ec2.startInstances(request);

		System.out.printf("Successfully started instance %s", instance_id);
	}
	
	
	public static void availableRegions() {
		
		System.out.println("Available regions ....");
		
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DescribeRegionsResult regions_response = ec2.describeRegions();

		for(Region region : regions_response.getRegions()) {
			System.out.printf(
				"[region] %15s, " +
				"[endpoint] %s\n",
				region.getRegionName(),
				region.getEndpoint());
		}
	}
	
	public static void stopInstance(String instance_id) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DryRunSupportedRequest<StopInstancesRequest> dry_request =
			() -> {
			StopInstancesRequest request = new StopInstancesRequest()
				.withInstanceIds(instance_id);

			return request.getDryRunRequest();
		};

		try {
			StopInstancesRequest request = new StopInstancesRequest()
				.withInstanceIds(instance_id);
	
			ec2.stopInstances(request);
			System.out.printf("Successfully stop instance %s\n", instance_id);

		} catch(Exception e)
		{
			System.out.println("Exception: "+e.toString());
		}

	}
	
	public static void createInstance(String ami_id) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		
		RunInstancesRequest run_request = new RunInstancesRequest()
			.withImageId(ami_id)
			.withInstanceType(InstanceType.T2Micro)
			.withMaxCount(1)
			.withMinCount(1);

		RunInstancesResult run_response = ec2.runInstances(run_request);

		String reservation_id = run_response.getReservation().getInstances().get(0).getInstanceId();

		System.out.printf(
			"Successfully started EC2 instance %s based on AMI %s",
			reservation_id, ami_id);
	
	}

	public static void rebootInstance(String instance_id) {
		
		System.out.printf("Rebooting .... %s\n", instance_id);
		
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		try {
			RebootInstancesRequest request = new RebootInstancesRequest()
					.withInstanceIds(instance_id);

				RebootInstancesResult response = ec2.rebootInstances(request);

				System.out.printf(
						"Successfully rebooted instance %s", instance_id);

		} catch(Exception e)
		{
			System.out.println("Exception: "+e.toString());
		}

		
	}
	
	public static void listImages() {
		System.out.println("Listing images....");
		
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		
		DescribeImagesRequest request = new DescribeImagesRequest();
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		
		// aws-htcondor-slave 이름을 가진 이미지만 검색됨. 하지만 필터를 지우면 작업 시간이 지나치게 길어지는 문제가 있었음.
		request.getFilters().add(new Filter().withName("name").withValues("aws-htcondor-slave")); 
		request.setRequestCredentialsProvider(credentialsProvider);
		
		DescribeImagesResult results = ec2.describeImages(request);
		
		for(Image images :results.getImages()){
			System.out.printf("[ImageID] %s, [Name] %s, [Owner] %s\n", 
					images.getImageId(), images.getName(), images.getOwnerId());
		}
	}

	public static void checkCondorStatus(String instanceId) {
    	try {
        	JSch jsch = new JSch();

			String publicDnsName = getPublicDnsName(instanceId);
    		if (publicDnsName == null) {
        		System.out.println("Could not find instance with ID: " + instanceId);
        		return;
    		}

        	// AWS EC2 인스턴스에 연결하기 위한 세션 생성
        	Session session = jsch.getSession("ec2-user", publicDnsName, 22);
        	session.setConfig("StrictHostKeyChecking", "no");
        	jsch.addIdentity("/home/an183244/Downloads/testtest.pem");
        	session.connect();

        	// SSH 채널을 열고 condor_status 명령어 실행
        	ChannelExec channel = (ChannelExec) session.openChannel("exec");
        	channel.setCommand("condor_status");
        	channel.setErrStream(System.err);
        	channel.connect();

        	// 결과 출력
        	InputStream input = channel.getInputStream();
        	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        	String line;
        	while ((line = reader.readLine()) != null) {
            	System.out.println(line);
        	}

        	// 채널과 세션 종료
        	channel.disconnect();
        	session.disconnect();
    	} catch (JSchException | IOException e) {
	        e.printStackTrace();
    	}
	}

	public static String getPublicDnsName(String instanceId) {
    	final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    	DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
    	DescribeInstancesResult response = ec2.describeInstances(request);

    	for (Reservation reservation : response.getReservations()) {
        	for (Instance instance : reservation.getInstances()) {
        	    if (instance.getInstanceId().equals(instanceId)) {
    	            return instance.getPublicDnsName();
	            }
        	}
	    }

	    return null;
	}

	public static void createImage(String instance_id) {
	    final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    	CreateImageRequest request = new CreateImageRequest()
        	.withInstanceId(instance_id)
        	.withName("createImageTest");

    	CreateImageResult response = ec2.createImage(request);

    	System.out.printf(
        	"Successfully created image with id %s from instance %s",
    		response.getImageId(), instance_id);
	}

	public static void listKeyPairs() {
    	final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    	DescribeKeyPairsResult response = ec2.describeKeyPairs();

    	for(KeyPairInfo key_pair : response.getKeyPairs()) {
        	System.out.printf(
            	"Found key pair with name %s and fingerprint %s\n",
        	    key_pair.getKeyName(), key_pair.getKeyFingerprint());
    	}
	}

	public static void createKeyPair(String key_name) {
    	final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    	CreateKeyPairRequest request = new CreateKeyPairRequest()
    	    .withKeyName(key_name);

    	CreateKeyPairResult response = ec2.createKeyPair(request);

    	String privateKey = response.getKeyPair().getKeyMaterial();

    	try (FileWriter writer = new FileWriter(key_name + ".pem")) {
	        writer.write(privateKey);
    	} catch (IOException e) {
	        e.printStackTrace();
    	}

	    System.out.printf(
        	"Successfully created key pair named %s",
    	    response.getKeyPair().getKeyName());
	}

	public static void listSecurityGroups() {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        DescribeSecurityGroupsRequest request = new DescribeSecurityGroupsRequest();
        DescribeSecurityGroupsResult response = ec2.describeSecurityGroups(request);

        for(SecurityGroup group : response.getSecurityGroups()) {
            System.out.printf(
                "Found security group with id %s, name %s and description %s\n",
                group.getGroupId(), group.getGroupName(), group.getDescription());
        }
    }

    public static void addRuleToSecurityGroup(String group_id, String protocol, int from_port, int to_port, String ip_range) {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        IpRange ipRangeObj = new IpRange().withCidrIp(ip_range);
        IpPermission ipPermission = new IpPermission()
            .withIpProtocol(protocol)
            .withFromPort(from_port)
            .withToPort(to_port);
        ipPermission.getIpv4Ranges().add(ipRangeObj);

        AuthorizeSecurityGroupIngressRequest request = new AuthorizeSecurityGroupIngressRequest()
            .withGroupId(group_id)
            .withIpPermissions(ipPermission);

        ec2.authorizeSecurityGroupIngress(request);

        System.out.printf(
            "Successfully added rule to security group %s", group_id);
    }
}
	
