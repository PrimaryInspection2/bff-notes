# bff-notes
## Prerequisites

Before applying k8s configs, install NGINX Ingress Controller:

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.10.0/deploy/static/provider/cloud/deploy.yaml

Verify installation:

kubectl get pods -n ingress-nginx