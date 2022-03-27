pipeline {

    agent any

    environment {
        BUILD_ENV = "nonprod"
        APP_NAME = "content-analyser"
        APP_ROOT_PATH = "content-analyser"
        IMAGE_NAME = "ms-$APP_NAME"
        IMAGE_TAG = "$BUILD_ENV-$BUILD_NUMBER"

        REMOTE_USER = "xxxxxxxx"
        REMOTE_HOST = "xx.xx.xx.xx"
        REMOTE_CERT = "$BUILD_ENV-xxxxxxxx"
        ECR_HOST = "xxxxxxxx"
        ECR_REGION = "xxxxxxxx"
    }

    stages {
        stage("Checkout") {
            steps {
                sh 'echo Branch checkout completed'
            }
        }

        stage("Clean") {
            steps {
                dir("$APP_ROOT_PATH") {
                    sh "mvn clean"
                }
            }
        }

        stage("Compile") {
            steps {
                dir("$APP_ROOT_PATH") {
                    sh "mvn compile"
                }
            }
        }

        stage("Test") {
            steps {
                dir("$APP_ROOT_PATH") {
                    sh "mvn test"
                }
            }
        }

        // fabric8 maven plugin is integrated with package goal of maven to create docker image
        stage("Package & Build docker image") {
            steps {
                dir("$APP_ROOT_PATH") {
                    sh "mvn package"
                }
            }
        }

        // Alternatively it can be pushed private registry or docker hub depending on architecture
        stage("Login ECR") {
            steps {
                sh "aws ecr get-login-password --region $ECR_REGION | docker login --username AWS --password-stdin $ECR_HOST"
            }
        }

        // Image is created with fabric8 plugin inside pom.xml. It will use application name as image name
        // Image is created with "APP_NAME" and later tagged with "IMAGE_NAME" so it can be pushed to ECR
        stage("Tag Image") {
            steps {
                sh "echo $BUILD_NUMBER"
                sh "docker tag $APP_NAME:latest $ECR_HOST/$IMAGE_NAME:$IMAGE_TAG"
            }
        }

        stage("Push to ECR") {
            steps {
                sh "docker push $ECR_HOST/$IMAGE_NAME:$IMAGE_TAG"
            }
        }

        // Assuming we have KOPS on-premise or cloud || EKS on AWS cloud
        stage("Deploy to cluster") {
            steps {
                script {
                    withCredentials([file(credentialsId: "$REMOTE_CERT", variable: "pemFile")]) {
                        dir("$APP_ROOT_PATH/ci-cd/kubernetes") {
                            sh "ssh -o StrictHostKeyChecking=no -i $pemFile $REMOTE_USER@$REMOTE_HOST 'rm -rf stateless-services; mkdir stateless-services;'"
                            sh "envsubst < app-manifest-$BUILD_ENV.yaml > $APP_NAME.yaml"
                            sh "scp -o StrictHostKeyChecking=no -i $pemFile $APP_NAME.yaml $REMOTE_USER@$REMOTE_HOST:/home/ec2-user/stateless-services/$APP_NAME.yaml"
                            sh "ssh -o StrictHostKeyChecking=no -i $pemFile $REMOTE_USER@$REMOTE_HOST 'cd /home/ec2-user/stateless-services; kubectl apply -f $APP_NAME.yaml;'"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Job completed. Cleaning up cache."

            dir("$WORKSPACE") {
                deleteDir()
            }

            dir("$WORKSPACE@tmp") {
                deleteDir()
            }

            dir("$WORKSPACE@script") {
                deleteDir()
            }

            sh "docker rmi -f $ECR_HOST/$IMAGE_NAME:$IMAGE_TAG"
        }

        success {
            echo "Build completed successfully"
        }

        failure {
            echo "Job failed! Deleting image from ECR"
            sh "aws ecr batch-delete-image --repository-name $IMAGE_NAME --image-ids imageTag=$IMAGE_TAG"
        }
    }
}