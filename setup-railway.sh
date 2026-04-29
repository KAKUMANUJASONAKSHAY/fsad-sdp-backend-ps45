#!/bin/bash
# Railway SDP Backend Deployment Configuration

# This script helps set up environment variables for Railway deployment

# Color codes
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}SDP Backend - Railway Deployment Setup${NC}"
echo "========================================"
echo ""

# Database Configuration
echo -e "${YELLOW}1. Database Configuration${NC}"
read -p "Database Host (e.g., mysql.example.com): " DB_HOST
read -p "Database Port (default 3306): " DB_PORT
DB_PORT=${DB_PORT:-3306}
read -p "Database Name: " DB_NAME
read -p "Database Username: " DB_USERNAME
read -sp "Database Password: " DB_PASSWORD
echo ""

# Email Configuration
echo ""
echo -e "${YELLOW}2. Email Configuration (Gmail)${NC}"
read -p "Email Address: " MAIL_USERNAME
read -sp "Gmail App Password (16 chars from myaccount.google.com/apppasswords): " MAIL_PASSWORD
echo ""

# JWT Configuration
echo ""
echo -e "${YELLOW}3. JWT Configuration${NC}"
read -p "JWT Secret (32+ characters, use 'openssl rand -base64 32' to generate): " JWT_SECRET
read -p "JWT Expiration (ms, default 600000=10min): " JWT_EXPIRATION_MS
JWT_EXPIRATION_MS=${JWT_EXPIRATION_MS:-600000}

# CORS Configuration
echo ""
echo -e "${YELLOW}4. CORS Configuration${NC}"
read -p "Frontend Domain (e.g., https://myapp.vercel.app): " CORS_ALLOWED_ORIGIN

# Razorpay Configuration
echo ""
echo -e "${YELLOW}5. Razorpay Configuration (Optional)${NC}"
read -p "Razorpay Key ID (leave empty to skip): " RAZORPAY_KEY_ID
if [ ! -z "$RAZORPAY_KEY_ID" ]; then
    read -sp "Razorpay Key Secret: " RAZORPAY_KEY_SECRET
    echo ""
fi

# Display Summary
echo ""
echo -e "${GREEN}Environment Variables Summary:${NC}"
echo "=================================="
echo "DB_URL=jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME"
echo "DB_USERNAME=$DB_USERNAME"
echo "MAIL_USERNAME=$MAIL_USERNAME"
echo "JWT_EXPIRATION_MS=$JWT_EXPIRATION_MS"
echo "CORS_ALLOWED_ORIGIN=$CORS_ALLOWED_ORIGIN"
echo ""

echo -e "${YELLOW}Next Steps:${NC}"
echo "1. Go to https://railway.app"
echo "2. Click 'New Project' → 'Deploy from GitHub'"
echo "3. Select: fsad-sdp-backend-ps45"
echo "4. In Variables section, add these values:"
echo "   - DB_URL=jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME"
echo "   - DB_USERNAME=$DB_USERNAME"
echo "   - DB_PASSWORD=[your password]"
echo "   - MAIL_USERNAME=$MAIL_USERNAME"
echo "   - MAIL_PASSWORD=[your gmail app password]"
echo "   - JWT_SECRET=$JWT_SECRET"
echo "   - JWT_EXPIRATION_MS=$JWT_EXPIRATION_MS"
echo "   - CORS_ALLOWED_ORIGIN=$CORS_ALLOWED_ORIGIN"
if [ ! -z "$RAZORPAY_KEY_ID" ]; then
    echo "   - RAZORPAY_KEY_ID=$RAZORPAY_KEY_ID"
    echo "   - RAZORPAY_KEY_SECRET=[your secret]"
fi
echo ""
echo -e "${GREEN}5. Click 'Deploy' and wait for completion${NC}"
echo ""
