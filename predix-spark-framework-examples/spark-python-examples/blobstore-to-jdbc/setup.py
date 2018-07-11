from setuptools import setup, find_packages
import os

setup(
        name='connect',
        version='1.0.0',
        description='multiplies by 10',
        packages=find_packages(),
        install_requires=['pyhocon', 'py4j'],
    	classifiers=[
    	 "connect.TestJob"
    	],
)

