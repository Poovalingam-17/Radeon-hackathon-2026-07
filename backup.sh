#!/bin/bash
echo "=== Starting GuardianAI Database Backup ==="

BACKUP_DIR="./backups"
mkdir -p $BACKUP_DIR
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/guardianai_backup_$TIMESTAMP.sql"

docker exec guardianai-mysql mysqldump -u root -proot guardianai_db > $BACKUP_FILE

echo "Backup complete! Saved to: $BACKUP_FILE"
