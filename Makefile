all:

build:
	docker-compose -f docker-compose.json build

up:
	docker-compose -f docker-compose.json up

down:
	docker-compose -f docker-compose.json down

ping:
	@echo $$(curl --silent http://127.0.0.1:8080/id) \
		$$(curl --silent http://127.0.0.1:8080/factorial?param=100) \
		$$(curl --silent http://127.0.0.1:8080/fibonacci?param=40)

	@echo $$(curl --silent http://127.0.0.1:8081/id) \
		$$(curl --silent http://127.0.0.1:8081/factorial?param=100) \
		$$(curl --silent http://127.0.0.1:8081/fibonacci?param=40)

test-low-compute:
	@echo $$(tput bold)$$(curl --silent http://127.0.0.1:8080/id)$$(tput sgr0) && ab -r -s 10000 -n 20000 -c 20000 http://127.0.0.1:8080/factorial?param=1 | grep "Requests per second"
	@echo "\n\n\n"
	@echo $$(tput bold)$$(curl --silent http://127.0.0.1:8081/id)$$(tput sgr0) && ab -r -s 10000 -n 20000 -c 20000 http://127.0.0.1:8081/factorialul?param=1 | grep "Requests per second"

test-high-compute:
	@echo $$(tput bold)$$(curl --silent http://127.0.0.1:8080/id)$$(tput sgr0) && ab -r -s 10000 -n 10 -c 10 http://127.0.0.1:8080/fibonacci?param=40 | grep "Requests per second"
	@echo "\n\n\n"
	@echo $$(tput bold)$$(curl --silent http://127.0.0.1:8081/id)$$(tput sgr0) && ab -r -s 10000 -n 10 -c 10 http://127.0.0.1:8081/fibonacci?param=40 | grep "Requests per second"
