# Generated by Django 3.2.3 on 2021-07-10 05:59

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='UserInfo',
            fields=[
                ('user_index', models.IntegerField(auto_created=True, primary_key=True, serialize=False)),
                ('user_email', models.CharField(default=False, max_length=20)),
                ('user_name', models.CharField(default=False, max_length=20)),
            ],
            options={
                'verbose_name': '유저정보 테이블',
                'db_table': 'user_info',
            },
        ),
    ]
