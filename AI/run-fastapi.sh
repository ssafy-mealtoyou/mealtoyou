#!/bin/bash

source ./.env

CONTAINER_ID=$(docker ps -aq -f name="${CONTAINER_NAME}")

if [ -n "${CONTAINER_ID}" ]; then
    # 컨테이너 ID를 기준으로 실제 이름 확인
    REAL_NAME=$(docker inspect --format='{{.Name}}' ${CONTAINER_ID} | sed 's/^\/\(.*\)/\1/')
    if [ "${REAL_NAME}" == "${CONTAINER_NAME}" ]; then
        echo "${CONTAINER_NAME} 컨테이너를 중지하고 제거합니다."
        docker stop ${CONTAINER_NAME}
        docker rm ${CONTAINER_NAME}
    else
        echo "정확한 이름의 컨테이너가 없습니다."
    fi
else
    echo "해당 이름의 컨테이너가 없습니다."
fi

# 해당 태그를 가진 이미지 ID들을 찾음
IMAGE_IDS=$(docker images | grep "$IMAGE_NAME" | awk '{print $3}')

# 이미지 ID들이 비어있지 않은 경우에만 삭제 실행
if [ ! -z "$IMAGE_IDS" ]; then
    echo "Deleting images with tag: $IMAGE_NAME"
    # 이미지 ID들을 삭제
    for ID in $IMAGE_IDS; do
        docker rmi -f $ID
    done
    echo "Deletion complete."
else
    echo "No images found with tag: $IMAGE_NAME"
fi

docker build -t ${IMAGE_NAME} .

docker run -d --name ${CONTAINER_NAME} -p ${PORT}:${PORT} ${IMAGE_NAME}
