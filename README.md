# groovy-raspberry
Groovy raspberry pi example

```sh
docker build . -t irrigation
docker volume create --name grapes-cache
docker run -it -v grapes-cache:/home/groovy/.groovy/grapes -d --name irrigation -v /sys:/sys -u root irrigation
```
